package lotto.controller

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
class LottoControllerPurchaseTest {
    @Test
    fun `결제승인을 통해 성공적으로 결제를 진행한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-id-1",
            orderId = "order-id-1",
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
    fun `중복된 orderId 를 사용하면 실패 한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-duplicate-1",
            orderId = "orderId-duplicate",
            lottoNumbers = listOf(listOf(1, 11, 17, 19, 21, 24))
        )

        sendRequest(
            request,
            "purchase-ticket-failure-duplicate-payment-key",
            commonRequestFields(),
            errorResponseFields(),
            400
        )
    }

    @Test
    fun `결제 임시 데이터가 없으면 실패 한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-id-2",
            orderId = "order-id-2",
            lottoNumbers = listOf(listOf(1, 11, 17, 19, 21, 24))
        )

        sendRequest(
            request,
            "purchase-ticket-failure-order-data-not-exist",
            commonRequestFields(),
            errorResponseFields(),
            400
        )
    }

    @Test
    fun `결제 임시 데이터와 금액이 다르면 실패 한다`() {
        val request = createRequest(
            amount = 1500,
            paymentKey = "paymentKey-duplicate",
            orderId = "orderId-duplicate",
            lottoNumbers = listOf(listOf(1, 11, 17, 19, 21, 24))
        )

        sendRequest(
            request,
            "purchase-ticket-failure-order-data-different-amount",
            commonRequestFields(),
            errorResponseFields(),
            400
        )
    }


    @Test
    fun `금액은 1000원 단위로 끊어져야 한다`() {
        val request = createRequest(
            amount = 1500,
            paymentKey = "paymentKey-id-2",
            orderId = "order-id-2",
            lottoNumbers = listOf(listOf(1, 11, 17, 19, 21, 24))
        )

        sendRequest(
            request,
            "purchase-ticket-failure-not-remainder-unit",
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
        RestAssured.given().log().all()
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
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        fieldWithPath("data.purchaseResponse").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("data.purchaseResponse.id").type(JsonFieldType.STRING).description("발행된 로또 번호"),
        fieldWithPath("data.purchaseResponse.amount").type(JsonFieldType.NUMBER).description("발행 일시")
    )

    private fun errorResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
    )
}
