package lotto.controller

import config.AcceptanceTest
import docs.*
import docs.field.DocsFieldType
import docs.field.means
import docs.field.type
import docs.field.value
import docs.request.body
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoOrder.json"])
class LottoOrderTest {
    @Test
    fun `결제승인을 통해 성공적으로 결제를 진행한다`() {
        DocsApiBuilder("order-success")
            .setRequest("/api/orders", HttpMethod.POST) {
                body {
                    field {
                        "numbers" type DocsFieldType.ARRAY(DocsFieldType.ARRAY(DocsFieldType.NUMBER)) means "구매할 로또 번호 목록" value
                                listOf(listOf(1, 10, 11, 14, 15, 17))
                    }
                }
            }
            .setResponse {
                body {
                    field { "lottoPublishId" type DocsFieldType.NUMBER means "승인할 로또 발행의 고유 식별자" }
                    field { "orderId" type DocsFieldType.STRING means "승인할 주문 번호" }
                }
            }
            .execute()
            .statusCode(200)
    }
}
