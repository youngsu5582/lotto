package docs.request

import docs.field.ApiField
import docs.field.toFieldDescriptors
import docs.field.toHeadDescriptors
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.payload.FieldDescriptor

class DslContainer {
    val headers = DslBuilder()
    val body = DslBuilder()
    val queryParams = DslBuilder()

    fun convertBody(): Map<String, Any> = convertFields(body.fields)
    fun convertHeaders(): Map<String, Any> = convertFields(headers.fields)
    fun convertQueryParams(): Map<String, Any> = convertFields(queryParams.fields)

    fun convertBodyDescriptors(): List<FieldDescriptor> = body.fields.toFieldDescriptors()
    fun convertHeadersDescriptors(): List<HeaderDescriptor> = headers.fields.toHeadDescriptors()
    fun convertQueryParamsDescriptors(): List<FieldDescriptor> = queryParams.fields.toFieldDescriptors()

    private fun convertFields(fields: List<ApiField>): Map<String, Any> {
        fun processField(field: ApiField): Any {
            return if (field.children.isNotEmpty()) {
                field.children.associate { it.name to processField(it) }
            } else {
                field.value
            }
        }
        return fields.associate { it.name to processField(it) }
    }

    fun printRequestInfo() {
        println("Headers:")
        headers.fields.forEach { printApiField(it, indent = "  ") }
        println("Body:")
        body.fields.forEach { printApiField(it, indent = "  ") }
        println("QueryParams:")
        queryParams.fields.forEach { printApiField(it, indent = "  ") }
    }

    private fun printApiField(field: ApiField, indent: String) {
        println("$indent- ${field.name} (${field.docsFieldType::class.simpleName}): ${field.description} ${field.value}")
        field.children.forEach { printApiField(it, "$indent  ") }
    }
}

// 컨테이너 확장 함수
fun DslContainer.headers(block: DslBuilder.() -> Unit) {
    headers.apply(block)
}

fun DslContainer.body(block: DslBuilder.() -> Unit) {
    body.apply(block)
}

fun DslContainer.params(block: DslBuilder.() -> Unit) {
    queryParams.apply(block)
}
