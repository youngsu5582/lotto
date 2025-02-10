package lotto.controller

import TestConstant
import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoCancel.json", "acceptance/member.json"])
class LottoPurchaseCancelTest {
    @Test
    fun `인증된 사용자가 아니면 결제 취소를 실패 한다`() {
        DocsApiBuilder("cancel-failure-not-authenticated")
            .setRequest("/api/cancel", HttpMethod.POST) {
                headers {
                    "Authorization" type DocsFieldType.STRING value "Bearer notValidToken"
                }
                body {
                    "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 2
                }
            }.setResponse {
            }.execute(true)
            .statusCode(401)
    }

    @Test
    fun `결제취소를 한다`() {
        DocsApiBuilder("cancel-success")
            .setRequest("/api/cancel", HttpMethod.POST) {
                headers {
                    "Authorization" type DocsFieldType.STRING value "Bearer ${TestConstant.TOKEN}"
                }
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
    fun `자기가 구매한 요소가 아니면 실패를 한다`() {
        DocsApiBuilder("cancel-failure-not-owner")
            .setRequest("/api/cancel", HttpMethod.POST) {
                headers {
                    "Authorization" type DocsFieldType.STRING value "Bearer ${TestConstant.TOKEN}"
                }
                body {
                    "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 2
                }
            }
            .execute()
            .statusCode(400)
    }

    @Test
    fun `결제취소 실패를 한다`() {
        DocsApiBuilder("cancel-failure")
            .setRequest("/api/cancel", HttpMethod.POST) {
                headers {
                    "Authorization" type DocsFieldType.STRING value "Bearer ${TestConstant.TOKEN}"
                }
                body {
                    "billId" type DocsFieldType.NUMBER means "구매했던 영수증 ID" value 3
                }
            }
            .execute()
            .statusCode(400)
    }
}
