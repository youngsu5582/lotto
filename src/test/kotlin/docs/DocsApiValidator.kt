package docs

import io.restassured.response.ExtractableResponse


class DocsApiValidator(private val response: ExtractableResponse<*>) {
    fun statusCode(expected: Int): DocsApiValidator {
        assert(response.statusCode() == expected)
        return this
    }

    fun hasValue(path: String, value: Any): DocsApiValidator {
        println("Assert value at '$path' is '$value'")
        return this
    }
}
