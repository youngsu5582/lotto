package docs

import io.restassured.response.ExtractableResponse


class DocsApiValidator(private val response: ExtractableResponse<*>) {
    fun statusCode(expected: Int): DocsApiValidator {
        assert(response.statusCode() == expected)
        return this
    }

    fun hasValue(path: String, value: Any): DocsApiValidator {
        val actual = response.path<Any>(path)
        assert(actual == value) { "Expected '$value' but was '$actual' at path '$path'" }
        return this
    }

    fun hasHeader(name: String, value: String): DocsApiValidator {
        val headerValue = response.header(name)
        assert(headerValue == value) { "Expected header '$name' to be '$value' but was '$headerValue'" }
        return this
    }

    fun hasResponseTime(maxMillis: Long): DocsApiValidator {
        val time = response.time()
        assert(time <= maxMillis) { "Response time $time ms exceeded maximum $maxMillis ms" }
        return this
    }

    fun hasArraySize(path: String, size: Int): DocsApiValidator {
        val array = response.path<List<*>>(path)
        assert(array.size == size) { "Expected array size $size but was ${array.size} at path '$path'" }
        return this
    }

    fun arrayContains(path: String, value: Any): DocsApiValidator {
        val array = response.path<List<*>>(path)
        assert(array.contains(value)) { "Array at path '$path' does not contain value '$value'" }
        return this
    }

    fun matchesPattern(path: String, regex: String): DocsApiValidator {
        val value = response.path<String>(path)
        assert(value.matches(Regex(regex))) { "Value '$value' at path '$path' does not match pattern '$regex'" }
        return this
    }

    fun hasType(path: String, type: Class<*>): DocsApiValidator {
        val value = response.path<Any>(path)
        assert(type.isInstance(value)) { "Value at path '$path' is not of type ${type.simpleName}" }
        return this
    }

    fun exists(path: String): DocsApiValidator {
        val value = response.path<Any?>(path)
        assert(value != null) { "Path '$path' does not exist in response" }
        return this
    }
}
