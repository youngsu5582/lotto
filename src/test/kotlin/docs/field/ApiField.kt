package docs.field

import docs.request.DslBuilder
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes

data class ApiField(
    val name: String,
    val docsFieldType: DocsFieldType,
    var value: Any,
    var description: String,
    var optional: Boolean,
    var children: List<ApiField> = emptyList()
)

infix fun String.type(docsFieldType: DocsFieldType): ApiField =
    ApiField(name = this, docsFieldType = docsFieldType, value = "", description = "", optional = false)

infix fun ApiField.means(description: String): ApiField {
    this.description = description
    return this
}

infix fun ApiField.value(value: Any): ApiField {
    this.value = value
    return this
}

infix fun ApiField.optional(flag: Boolean): ApiField {
    this.optional = flag
    return this
}

// 하위 필드를 추가할 때, 여러 ApiField를 반환하도록 하기 위해 블록은 List<ApiField>를 반환합니다.
infix fun ApiField.withChildren(block: DslBuilder.() -> Unit): ApiField {
    val childBuilder = DslBuilder()
    childBuilder.block()
    this.children = childBuilder.fields
    return this
}

fun List<ApiField>.toFieldDescriptors(): List<FieldDescriptor> {
    val descriptors = mutableListOf<FieldDescriptor>()

    fun processField(field: ApiField, parentPath: String = "") {
        val fullPath = if (parentPath.isEmpty()) field.name else "$parentPath.${field.name}"

        fun getArrayPath(type: DocsFieldType, path: String): String {
            return when (type) {
                is DocsFieldType.ARRAY -> getArrayPath(type.elementType, "$path[]")
                else -> path
            }
        }

        val formattedPath = getArrayPath(field.docsFieldType, fullPath)

        val descriptor = fieldWithPath(formattedPath)
            .type(field.docsFieldType.type)
            .description(
                field.description + if (field.docsFieldType is DocsFieldType.ARRAY) {
                    " (요소 타입: ${getArrayTypeString(field.docsFieldType.elementType)})"
                } else "" + (field.docsFieldType.format?.let { " (형식: $it)" } ?: "")
            )
            .attributes(
                Attributes.Attribute("optional", field.optional.toString().uppercase())
            )

        descriptors.add(descriptor)

        field.children.forEach { processField(it, formattedPath) }
    }

    this.forEach { processField(it) }
    return descriptors
}


fun getArrayTypeString(type: DocsFieldType): String {
    return when (type) {
        is DocsFieldType.ARRAY -> "Array<${getArrayTypeString(type.elementType)}>"
        is DocsFieldType.NUMBER -> "Number"
        is DocsFieldType.STRING -> "String"
        is DocsFieldType.BOOLEAN -> "Boolean"
        is DocsFieldType.OBJECT -> "Object"
        else -> "Unknown"
    }
}


fun List<ApiField>.toHeadDescriptors(): List<HeaderDescriptor> {
    val descriptors = mutableListOf<HeaderDescriptor>()
    this.forEach { field ->
        processApiField(field) { path, apiField ->
            val descriptor = HeaderDocumentation.headerWithName(path)
                .description(field.description)
                .attributes(
                    Attributes.Attribute("optional", field.optional.toString().uppercase())
                )
            descriptors.add(descriptor)
        }
    }
    return descriptors
}

fun processApiField(
    field: ApiField,
    parentPath: String = "",
    processor: (String, ApiField) -> Unit
) {
    val fullPath = if (parentPath.isEmpty()) field.name else "$parentPath.${field.name}"
    processor(fullPath, field)
    field.children.forEach { processApiField(it, fullPath, processor) }
}
