package lotto.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import docs.field.DocsFieldType.NUMBER
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoRoundInfo.json", "/acceptance/lottoStatistics.json"])
class LottoStatisticsTest {
    @Test
    fun `현재, 회차에 대한 정보를 반환한다`() {
        DocsApiBuilder("lotto-round-info-statistics")
            .setRequest("/api/lottoes/statistics", HttpMethod.GET) {}
            .setResponse {
                body {
                    "lottoRoundInfo" type NUMBER means "로또 회차 번호"
                    "memberCount" type NUMBER means "구매한 멤버 수"
                    "lottoPublishCount" type NUMBER means "결제된 로또 용지 수"
                    "totalPurchaseMoney" type NUMBER means "총 결제된 금액"
                    "updatedAt" type DocsFieldType.DATETIME means "통게가 업데이트 된 시간"
                }
            }.execute()
            .statusCode(200)
    }
}
