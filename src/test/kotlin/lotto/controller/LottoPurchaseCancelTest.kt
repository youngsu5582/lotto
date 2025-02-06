package lotto.controller

import config.AcceptanceTest
import docs.*
import docs.field.*
import docs.request.body
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoCancel.json"])
class LottoPurchaseCancelTest {
    @Test
    fun `결제취소를 한다`() {
        DocsApiBuilder("cancel-success")
            .setRequest("/api/cancel", HttpMethod.POST) {
                body {
                    field { "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 1 }
                    field {
                        "sample" type DocsFieldType.OBJECT means "샘플 객체" withChildren {
                            field { "id" type DocsFieldType.STRING means "취소된 결제의 고유 식별자" value "ID" optional true }
                            field { "amount" type DocsFieldType.NUMBER means "취소된 결제 금액" value 1 }
                        }
                    }
                }
            }
            .setResponse {
                body {
                    field {
                        "purchaseResponse" type DocsFieldType.OBJECT means "응답 데이터" withChildren {
                            field { "id" type DocsFieldType.STRING means "취소된 결제의 고유 식별자" }
                            field { "amount" type DocsFieldType.NUMBER means "취소된 결제 금액" }
                        }
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
                    field { "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 2 }
                }
            }
            .execute()
            .statusCode(400)
    }
}
