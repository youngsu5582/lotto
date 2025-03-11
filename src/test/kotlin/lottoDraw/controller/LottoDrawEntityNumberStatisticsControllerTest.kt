package lottoDraw.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/lottoNumberCount.json", "/acceptance/lottoDraw.json"])
class LottoDrawEntityNumberStatisticsControllerTest {
    @Test
    fun `번호를 통해 유사 당첨 번호 및 번호 통계를 응답한다`() {
        DocsApiBuilder("draw-number-statistics")
            .setRequest("/api/draw/statistics/numbers", HttpMethod.GET) {
                params {
                    "numbers" type DocsFieldType.ARRAY(DocsFieldType.NUMBER) means "조회할 숫자 목록" value
                            listOf(10, 13, 15, 17, 20, 21)
                }
            }
            .setResponse {
                body {
                    "lottoNumberCountResponse" type DocsFieldType.ARRAY() withChildren {
                        "number" type DocsFieldType.NUMBER means "로또 번호"
                        "count" type DocsFieldType.NUMBER means "로또 번호가 등록된 회수"
                    }
                    "lottoResultResponse" type DocsFieldType.ARRAY() withChildren {
                        "round" type DocsFieldType.NUMBER means "로또 번호"
                        "lottoDrawResponse" type DocsFieldType.OBJECT withChildren {
                            "date" type DocsFieldType.DATE means "응모된 날짜"
                            "numbers" type DocsFieldType.ARRAY(DocsFieldType.NUMBER) means "응모된 숫자"
                            "bonus" type DocsFieldType.NUMBER means "보너스 숫자"
                        }
                        "drawResultResponse" type DocsFieldType.OBJECT withChildren {
                            "matchCount" type DocsFieldType.NUMBER means "일치한 숫자"
                            "bonusMatch" type DocsFieldType.BOOLEAN means "보너스 일치 여부"
                        }
                    }
                }
            }
            .execute(true)
            .statusCode(200)
    }
}
