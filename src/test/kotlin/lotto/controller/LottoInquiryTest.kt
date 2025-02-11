package lotto.controller

import TestConstant
import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import lotto.domain.entity.LottoPublishStatus
import lotto.domain.entity.LottoStatus
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/member.json", "/acceptance/lottoBills.json"])
class LottoInquiryTest {
    @Test
    fun `자기가 구매한 로또를 조회한다`() {
        DocsApiBuilder("inquiry-success")
            .setRequest("/api/lottoes", HttpMethod.GET) {
                headers {
                    "Authorization" type DocsFieldType.STRING value "Bearer ${TestConstant.TOKEN}"
                }
            }
            .setResponse {
                body {
                    "response" type DocsFieldType.ARRAY(DocsFieldType.OBJECT) means "반환 객체" withChildren {
                        "billId" type DocsFieldType.NUMBER means "구매한 영수증 식별자"
                        "paymentResponse" type DocsFieldType.OBJECT means "결제 응답 객체" withChildren {
                            "id" type DocsFieldType.STRING means "결제 식별자"
                            "amount" type DocsFieldType.NUMBER means "결제한 금액"
                        }
                        "publishResponse" type DocsFieldType.OBJECT means "발행한 로또 객체" withChildren {
                            "status" type DocsFieldType.ENUM.of<LottoPublishStatus>() means "발행된 로또의 상태"
                            "lottoRoundInfo" type DocsFieldType.OBJECT means "발행된 회차 객체" withChildren {
                                "round" type DocsFieldType.NUMBER means "회차 번호"
                                "startDate" type DocsFieldType.DATETIME means "회차 시작 시간"
                                "endDate" type DocsFieldType.DATETIME means "회차 종료 시간"
                                "drawDate" type DocsFieldType.DATETIME means "회차 응모 시간"
                                "paymentDeadline" type DocsFieldType.DATETIME means "회차 응모 시간"
                                "status" type DocsFieldType.ENUM.of<LottoStatus>() means "회차의 상태"
                            }
                            "issuedAt" type DocsFieldType.STRING means "발행된 시간"
                            "lottoes" type DocsFieldType.ARRAY(DocsFieldType.ARRAY(DocsFieldType.NUMBER)) means "로또 번호"
                        }
                    }
                }
            }
            .execute(true)
            .statusCode(200)
    }
}
