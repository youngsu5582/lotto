package docs.field

import org.springframework.restdocs.payload.JsonFieldType

sealed class DocsFieldType(val type: JsonFieldType) {
    open val format: String? = null

    object NUMBER : DocsFieldType(JsonFieldType.NUMBER)
    object STRING : DocsFieldType(JsonFieldType.STRING)
    object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)
    object OBJECT : DocsFieldType(JsonFieldType.OBJECT)
    object DATE : DocsFieldType(JsonFieldType.STRING) {
        override val format = "yyyy-MM-dd"
    }
    object DATETIME : DocsFieldType(JsonFieldType.STRING) {
        override val format = "yyyy-MM-dd HH:mm:ss"
    }

    data class ARRAY(val elementType: DocsFieldType) : DocsFieldType(JsonFieldType.ARRAY)

    data class ENUM<T : Enum<T>>(val enums: Collection<T>) : DocsFieldType(JsonFieldType.STRING) {
        companion object {
            inline fun <reified T : Enum<T>> of(): ENUM<T> {
                return ENUM(T::class.java.enumConstants.asList())
            }
        }
    }
}
