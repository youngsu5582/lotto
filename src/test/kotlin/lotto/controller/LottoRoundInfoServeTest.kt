package lotto.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import docs.field.DocsFieldType.NUMBER
import lotto.domain.entity.LottoStatus
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoRoundInfo.json"])
class LottoRoundInfoServeTest {
    @Test
    fun `현재, 회차에 대한 정보를 반환한다`() {
        DocsApiBuilder("lotto-round-info-serve")
            .setRequest("/api/lottoes/round-info", HttpMethod.GET) {}
            .setResponse {
                body {
                    "round" type NUMBER means "로또 회차 번호"
                    "endDate" type DocsFieldType.DATETIME means "구매 종료 기한"
                    "drawDate" type DocsFieldType.DATETIME means "응모 기한"
                    "status" type DocsFieldType.ENUM.of<LottoStatus>() means "회차 상태"
                }
            }.execute()
            .statusCode(200)
    }
}
