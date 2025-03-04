package docs

import docs.request.DslContainer
import io.restassured.RestAssured
import io.restassured.filter.Filter
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation
import org.springframework.restdocs.snippet.SnippetException

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

    private val capturedResponse: ThreadLocal<Response> = ThreadLocal()

    private val capturingFilter = Filter { requestSpec, responseSpec, ctx ->
        val response = ctx.next(requestSpec, responseSpec)
        capturedResponse.set(response)
        response
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
                .filter(
                    RestAssuredRestDocumentation.document(
                        documentName,
                        HeaderDocumentation.requestHeaders(requestContainer.convertHeadersDescriptors()),
                        PayloadDocumentation.requestFields(requestContainer.convertBodyDescriptors()),
                    )
                )
                .headers(requestContainer.convertHeaders())
                .queryParams(requestContainer.convertQueryParams())
                .body(requestContainer.convertBody())
            val response = requestSpec
                .filter(
                    RestAssuredRestDocumentation.document(
                        documentName,
                        HeaderDocumentation.responseHeaders(responseContainer.convertHeadersDescriptors()),
                        SUCCESS_SNIPPET.andWithPrefix("data.", responseContainer.convertBodyDescriptors())
                    )
                )
                .filter(capturingFilter)
                .request(method.toMethod(), endpoint)
                .then()
                .extract()
            return DocsApiValidator(response)
        } catch (snippetException: SnippetException) {
            val captured = capturedResponse.get()?.asString() ?: "No response captured."
            println("Captured Response: $captured")
            throw IllegalArgumentException("API 문서화 중 오류가 발생했습니다: ${snippetException.message}", snippetException)
        } catch (e: Exception) {
            throw IllegalStateException("실행중 요류가 발생했습니다 : ${e.message}", e)
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
