package docs.request

import docs.field.ApiField
import docs.field.DocsFieldType

class DslBuilder {
    internal val fields = mutableListOf<ApiField>()

    infix fun String.type(docsFieldType: DocsFieldType): ApiField {
        val field = ApiField(
            name = this, docsFieldType = docsFieldType,
            value = "",
            description = "",
            optional = false,
        )
        fields.add(field)
        return field
    }
}
