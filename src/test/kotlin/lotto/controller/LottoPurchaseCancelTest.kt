package lotto.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import docs.field.means
import docs.field.value
import docs.field.withChildren
import docs.request.body
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoCancel.json"])
class LottoPurchaseCancelTest {
    @Test
    fun `결제취소를 한다`() {
        DocsApiBuilder("cancel-success")
            .setRequest("/api/cancel", HttpMethod.POST) {
                body {
                    "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 1
                }
            }
            .setResponse {
                body {
                    "purchaseResponse" type DocsFieldType.OBJECT means "응답 데이터" withChildren {
                        "id" type DocsFieldType.STRING means "취소된 결제의 고유 식별자"
                        "amount" type DocsFieldType.NUMBER means "취소된 결제 금액"
                    }
                }
            }
            .execute()
            .statusCode(200)
    }

    @Test
    fun `결제취소 실패를 한다`() {
        DocsApiBuilder("cancel-failure")
            .setRequest("/api/cancel", HttpMethod.POST) {
                body {
                    "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 2
                }
            }
            .execute()
            .statusCode(400)
    }
}
