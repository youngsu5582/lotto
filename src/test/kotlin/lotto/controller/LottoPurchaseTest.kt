package lotto.controller

import config.AcceptanceTest
import docs.*
import docs.field.*
import docs.request.DslContainer
import docs.request.body
import docs.request.headers
import lotto.domain.vo.Currency
import lotto.domain.vo.PurchaseType
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoPurchase.json"])
class LottoPurchaseTest {
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
                    field {
                        "purchaseResponse" type DocsFieldType.OBJECT means "응답 데이터" withChildren {
                            field { "id" type DocsFieldType.STRING means "취소된 결제의 고유 식별자" }
                            field { "amount" type DocsFieldType.NUMBER means "취소된 결제 금액" }
                        }
                    }
                }
            }.execute()
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
        paymentErrorCode: String = ""
    ): DslContainer {
        return DslContainer().apply {
            body {
                field { "lottoPublishId" type DocsFieldType.NUMBER means "주문한 영수증 ID" value lottoPublishId }
                field {
                    "purchaseHttpRequest" type DocsFieldType.OBJECT means "결제 승인 HTTP 객체" withChildren {
                        field { "purchaseType" type DocsFieldType.ENUM.of<PurchaseType>() means "구매 유형" value purchaseType }
                        field { "currency" type DocsFieldType.ENUM.of<Currency>() means "결제할 통화 유형" value currency }
                        field { "amount" type DocsFieldType.NUMBER means "취소할 결제 금액" value amount }
                        field { "orderId" type DocsFieldType.STRING means "취소할 주문 번호" value orderId }
                        field { "paymentKey" type DocsFieldType.STRING means "취소할 결제 식별자 - 결제 시스템 제공" value paymentKey }
                    }
                }
            }
            headers {
                if (paymentErrorCode.isNotBlank()) field { "Payment-Error-Header" type DocsFieldType.STRING means "토스 임의 에러 코드" value paymentErrorCode optional true }
            }
        }
    }
}
