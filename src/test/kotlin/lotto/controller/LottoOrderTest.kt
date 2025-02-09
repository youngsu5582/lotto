package lotto.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoOrder.json"])
class LottoOrderTest {
    @Test
    fun `결제승인을 통해 성공적으로 결제를 진행한다`() {
        DocsApiBuilder("order-success")
            .setRequest("/api/orders", HttpMethod.POST) {
                body {
                    "numbers" type DocsFieldType.ARRAY(DocsFieldType.ARRAY(DocsFieldType.NUMBER)) means "구매할 로또 번호 목록" value
                            listOf(listOf(1, 10, 11, 14, 15, 17))
                }
            }
            .setResponse {
                body {
                    "lottoPublishId" type DocsFieldType.NUMBER means "승인할 로또 발행의 고유 식별자"
                    "orderId" type DocsFieldType.STRING means "승인할 주문 번호"
                }
            }
            .execute(true)
            .statusCode(200)
    }
}
