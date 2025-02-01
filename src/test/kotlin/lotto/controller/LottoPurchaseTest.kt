package lotto.controller

import config.AcceptanceTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import sendRequest

@AcceptanceTest(["/acceptance/lottoPurchase.json"])
class LottoPurchaseTest {
    @Test
    fun `결제승인을 통해 성공적으로 결제를 진행한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-id-1",
            orderId = "order-id-1",
            lottoPublishId = 1
        )

        sendRequest(
            request,
            "purchase-ticket-success",
            commonRequestFields(),
            successResponseFields(),
            200,
            "/api/tickets"
        )
    }

    @Test
    @Disabled("현재는 orderId 나 PaymentKey 의 중복을 처리하지 않는다. 이는 다음 개선 사항으로 남긴다.")
    fun `중복된 orderId 를 사용하면 실패 한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-duplicate-1",
            orderId = "orderId-duplicate",
            lottoPublishId = 1
        )

        sendRequest(
            request,
            "purchase-ticket-failure-duplicate-payment-key",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/tickets"
        )
    }

    @Test
    fun `결제 임시 데이터가 없으면 실패 한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-not-exist",
            orderId = "order-not-exist",
            lottoPublishId = 1
        )

        sendRequest(
            request,
            "purchase-ticket-failure-order-data-not-exist",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/tickets"
        )
    }

    @Test
    fun `결제 임시 데이터와 금액이 다르면 실패 한다`() {
        val request = createRequest(
            amount = 1500,
            paymentKey = "paymentKey-duplicate",
            orderId = "orderId-duplicate",
            lottoPublishId = 1
        )

        sendRequest(
            request,
            "purchase-ticket-failure-order-data-different-amount",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/tickets"
        )
    }

    @Test
    fun `금액은 1000원 단위로 끊어져야 한다`() {
        val request = createRequest(
            amount = 1500,
            paymentKey = "paymentKey-id-2",
            orderId = "order-id-2",
            lottoPublishId = 1
        )

        sendRequest(
            request,
            "purchase-ticket-failure-not-remainder-unit",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/tickets"
        )
    }

    @Test
    fun `결제 제공자에 문제가 있으면 실패한다`() {
        val request = createRequest(
            amount = 1000,
            paymentKey = "paymentKey-id-2",
            orderId = "order-id-2",
            lottoPublishId = 1
        )

        sendRequest(
            request,
            "purchase-ticket-failure-purchase-provider-invalid",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/tickets",
            mapOf(Pair("Payment-Error-Header", "EXCEED_MAX_ONE_DAY_AMOUNT")),
        )
    }


    private fun createRequest(
        purchaseType: String = "CARD",
        currency: String = "KRW",
        amount: Int,
        paymentKey: String,
        orderId: String,
        lottoPublishId: Long
    ): Map<String, Any> {
        return mapOf(
            "purchaseHttpRequest" to mapOf(
                "purchaseType" to purchaseType,
                "currency" to currency,
                "amount" to amount,
                "paymentKey" to paymentKey,
                "orderId" to orderId
            ),
            "lottoPublishId" to lottoPublishId
        )
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
        fieldWithPath("lottoPublishId").type(JsonFieldType.NUMBER).description("퍼블리싱 한 로또 번호"),
    )

    private fun successResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        fieldWithPath("data.purchaseResponse").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("data.purchaseResponse.id").type(JsonFieldType.STRING).description("발행된 로또 영수증의 식별자"),
        fieldWithPath("data.purchaseResponse.amount").type(JsonFieldType.NUMBER).description("결제된 금액")
    )

    private fun errorResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
    )
}
