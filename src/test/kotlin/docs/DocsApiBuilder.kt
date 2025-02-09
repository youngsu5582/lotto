package docs

import docs.request.DslContainer
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

class DocsApiBuilder(private val documentName: String) {

    private var endpoint: String = ""
    private var requestContainer: DslContainer = DslContainer()
    private var method: HttpMethod = HttpMethod.POST
    private var responseContainer: DslContainer = DslContainer()

    companion object {
        private val SUCCESS_SNIPPET = PayloadDocumentation.responseFields(
            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
        )
    }

    fun setRequest(
        endpoint: String,
        method: HttpMethod = HttpMethod.POST,
        block: DslContainer.() -> Unit
    ): DocsApiBuilder {
        this.endpoint = endpoint
        this.method = method
        this.requestContainer = DslContainer().apply(block)
        return this
    }

    fun setRequestContainer(
        endpoint: String,
        method: HttpMethod = HttpMethod.POST,
        container: () -> DslContainer // 람다를 실행해야 함
    ): DocsApiBuilder {
        this.endpoint = endpoint
        this.method = method
        this.requestContainer = container()
        return this
    }

    fun setResponse(block: DslContainer.() -> Unit): DocsApiBuilder {
        this.responseContainer = DslContainer().apply(block)
        return this
    }

    fun execute(log: Boolean = false): DocsApiValidator {
        if (log) {
            printLog()
        }
        try {
            var requestSpec: RequestSpecification = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .headers(requestContainer.convertHeaders())
                .queryParams(requestContainer.convertQueryParams())
                .body(requestContainer.convertBody())
            val response = requestSpec
                .filter(
                    RestAssuredRestDocumentation.document(
                        documentName,
                        HeaderDocumentation.requestHeaders(requestContainer.convertHeadersDescriptors()),
                        HeaderDocumentation.responseHeaders(responseContainer.convertHeadersDescriptors()),
                        PayloadDocumentation.requestFields(requestContainer.convertBodyDescriptors()),
                        SUCCESS_SNIPPET.andWithPrefix("data.", responseContainer.convertBodyDescriptors())
                    )
                )
                .request(method.toMethod(), endpoint)
                .then().log().all()
                .extract()
            return DocsApiValidator(response)
        } catch (e: Exception) {
            throw IllegalStateException("API 문서화 중 오류가 발생했습니다: ${e.message}", e)
        }
    }

    private fun printLog() {
        println("=== DocsApiBuilder ===")
        println("Document: $documentName")
        println("Endpoint: $endpoint")
        println("Request: =================================")
        requestContainer.printRequestInfo()
        println("Response: =================================")
        responseContainer.printRequestInfo()
    }
}
