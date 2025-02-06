package docs.request

import docs.field.ApiField

class DslBuilder {
    internal val fields = mutableListOf<ApiField>()

    fun field(block: () -> ApiField) {
        fields.add(block())
    }
}
