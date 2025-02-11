package lotto.controller

import TestConstant
import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType.*
import docs.request.DslContainer
import lotto.domain.vo.Currency
import lotto.domain.vo.PurchaseType
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoPurchase.json", "/acceptance/member.json"])
class LottoPurchaseTest {
    @Test
    fun `인증된 사용자가 아니면 결제를 실패 한다`() {
        DocsApiBuilder("purchase-ticket-failure-not-authenticated")
            .setRequestContainer("/api/tickets", HttpMethod.POST) {
                createRequest(
                    amount = 1000,
                    paymentKey = "paymentKey-id-1",
                    orderId = "order-id-1",
                    lottoPublishId = 1,
                    token = "notValidToken"
                )
            }.setResponse {
            }.execute(true)
            .statusCode(401)
    }

    @Test
    fun `결제승인을 통해 성공적으로 결제를 진행한다`() {
        DocsApiBuilder("purchase-ticket-success")
            .setRequestContainer("/api/tickets", HttpMethod.POST) {
                createRequest(
                    amount = 1000,
                    paymentKey = "paymentKey-id-1",
                    orderId = "order-id-1",
                    lottoPublishId = 1
                )
            }.setResponse {
                body {
                    "purchaseResponse" type OBJECT means "응답 데이터" withChildren {
                        "id" type STRING means "취소된 결제의 고유 식별자"
                        "amount" type NUMBER means "취소된 결제 금액"
                    }
                }
            }.execute(true)
            .statusCode(200)
    }

    @Test
    fun `결제 임시 데이터가 없으면 실패 한다`() {
        DocsApiBuilder("purchase-ticket-failure-order-data-not-exist")
            .setRequestContainer("/api/tickets", HttpMethod.POST) {
                createRequest(
                    amount = 1000,
                    paymentKey = "paymentKey-not-exist",
                    orderId = "order-not-exist",
                    lottoPublishId = 1
                )
            }.execute()
            .statusCode(400)
    }

    @Test
    fun `결제 임시 데이터와 금액이 다르면 실패 한다`() {
        DocsApiBuilder("purchase-ticket-failure-order-data-different-amount")
            .setRequestContainer("/api/tickets", HttpMethod.POST) {
                createRequest(
                    amount = 1500,
                    paymentKey = "paymentKey-duplicate",
                    orderId = "orderId-duplicate",
                    lottoPublishId = 1
                )
            }.execute()
            .statusCode(400)
    }

    @Test
    fun `금액은 1000원 단위로 끊어져야 한다`() {
        DocsApiBuilder("purchase-ticket-failure-not-remainder-unit")
            .setRequestContainer("/api/tickets", HttpMethod.POST) {
                createRequest(
                    amount = 1500,
                    paymentKey = "paymentKey-id-2",
                    orderId = "order-id-2",
                    lottoPublishId = 1
                )
            }.execute()
            .statusCode(400)
    }

    @Test
    fun `결제 제공자에 문제가 있으면 실패한다`() {
        createRequest(
            amount = 1000,
            paymentKey = "paymentKey-id-2",
            orderId = "order-id-2",
            lottoPublishId = 1
        )

        DocsApiBuilder("purchase-ticket-failure-purchase-provider-invalid")
            .setRequestContainer("/api/tickets", HttpMethod.POST) {
                createRequest(
                    amount = 1000,
                    paymentKey = "paymentKey-id-2",
                    orderId = "order-id-2",
                    lottoPublishId = 1,
                    paymentErrorCode = "EXCEED_MAX_ONE_DAY_AMOUNT"
                )
            }.execute()
            .statusCode(400)
    }

//    @Test
//    @Disabled("현재는 orderId 나 PaymentKey 의 중복을 처리하지 않는다. 이는 다음 개선 사항으로 남긴다.")
//    fun `중복된 orderId 를 사용하면 실패 한다`() {
//        val request = createRequest(
//            amount = 1000,
//            paymentKey = "paymentKey-duplicate-1",
//            orderId = "orderId-duplicate",
//            lottoPublishId = 1
//        )
//
//        sendRequest(
//            request,
//            "purchase-ticket-failure-duplicate-payment-key",
//            commonRequestFields(),
//            errorResponseFields(),
//            400,
//            "/api/tickets"
//        )
//    }
//

    private fun createRequest(
        purchaseType: String = "CARD",
        currency: String = "KRW",
        amount: Int,
        paymentKey: String,
        orderId: String,
        lottoPublishId: Long,
        paymentErrorCode: String = "",
        token: String = TestConstant.TOKEN

    ): DslContainer {
        return DslContainer().apply {
            headers {
                "Authorization" type STRING value "Bearer $token" means "인증을 위한 토큰"
            }
            body {
                "lottoPublishId" type NUMBER means "주문한 영수증 ID" value lottoPublishId
                "purchaseHttpRequest" type OBJECT means "결제 승인 HTTP 객체" withChildren {
                    "purchaseType" type ENUM.of<PurchaseType>() means "구매 유형" value purchaseType
                    "currency" type ENUM.of<Currency>() means "결제할 통화 유형" value currency
                    "amount" type NUMBER means "취소할 결제 금액" value amount
                    "orderId" type STRING means "취소할 주문 번호" value orderId
                    "paymentKey" type STRING means "취소할 결제 식별자 - 결제 시스템 제공" value paymentKey
                }
            }
            headers {
                if (paymentErrorCode.isNotBlank()) "Payment-Error-Header" type STRING means "토스 임의 에러 코드" value paymentErrorCode optional true
            }
        }
    }
}
