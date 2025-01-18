package lotto

import config.AcceptanceTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

@AcceptanceTest(["/acceptance/lottoPurchase.json"])
class LottoControllerTest {
    @Test
    fun `결제를 한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentTestKey",
            orderId = "order-random-id-1",
            lottoNumbers = listOf(listOf(1, 11, 17, 19, 21, 24))
        )

        sendRequest(
            request,
            "purchase-ticket-success",
            commonRequestFields(),
            successResponseFields(),
            200
        )
    }

    @Test
    fun `결제 실패를 한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "duplicatePaymentKey",
            orderId = "order-random-id-2",
            lottoNumbers = listOf(listOf(1, 11, 17, 19, 21, 24))
        )

        sendRequest(
            request,
            "purchase-ticket-failure",
            commonRequestFields(),
            errorResponseFields(),
            400
        )
    }

    private fun createRequest(
        purchaseType: String = "CARD",
        currency: String = "KRW",
        amount: Int,
        paymentKey: String,
        orderId: String,
        lottoNumbers: List<List<Int>>
    ): Map<String, Any> {
        return mapOf(
            "purchaseHttpRequest" to mapOf(
                "purchaseType" to purchaseType,
                "currency" to currency,
                "amount" to amount,
                "paymentKey" to paymentKey,
                "orderId" to orderId
            ),
            "lottoRequest" to mapOf(
                "numbers" to lottoNumbers
            )
        )
    }

    private fun sendRequest(
        request: Map<String, Any>,
        documentName: String,
        requestFields: RequestFieldsSnippet,
        responseFields: ResponseFieldsSnippet,
        expectedStatus: Int
    ) {
        val response = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .filter(
                RestAssuredRestDocumentation.document(
                    documentName,
                    requestFields,
                    responseFields
                )
            )
            .post("/api/tickets").then().log().all().statusCode(expectedStatus).extract()
        println(response.body())
        println(response.statusCode())
    }

    private fun commonRequestFields() = requestFields(
        fieldWithPath("purchaseHttpRequest").type(JsonFieldType.OBJECT).description("구매 요청 정보"),
        fieldWithPath("purchaseHttpRequest.purchaseType").type(JsonFieldType.STRING)
            .description("구매 유형 (CARD, CASH 등)"),
        fieldWithPath("purchaseHttpRequest.currency").type(JsonFieldType.STRING)
            .description("결제 통화 (KRW, USD 등)"),
        fieldWithPath("purchaseHttpRequest.amount").type(JsonFieldType.NUMBER).description("결제 금액"),
        fieldWithPath("purchaseHttpRequest.paymentKey").type(JsonFieldType.STRING)
            .description("결제 키 (결제 시스템에서 제공)"),
        fieldWithPath("purchaseHttpRequest.orderId").type(JsonFieldType.STRING)
            .description("주문 ID (결제 시스템에서 제공)"),
        fieldWithPath("lottoRequest").type(JsonFieldType.OBJECT).description("로또 요청 정보"),
        fieldWithPath("lottoRequest.numbers").type(JsonFieldType.ARRAY).description("구매할 로또 번호 목록"),
        fieldWithPath("lottoRequest.numbers[].[]").type(JsonFieldType.ARRAY).description("로또 번호")
    )

    private fun successResponseFields() = responseFields(
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        fieldWithPath("data.purchaseResponse").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("data.purchaseResponse.id").type(JsonFieldType.STRING).description("발행된 로또 번호"),
        fieldWithPath("data.purchaseResponse.amount").type(JsonFieldType.NUMBER).description("발행 일시")
    )

    private fun errorResponseFields() = responseFields(
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
    )
}
