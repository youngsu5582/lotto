package member.controller

import TestConstant
import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/member.json"])
class MemberInfoTest {
    @Test
    fun `토큰을 통해 사용자의 정보를 받는다`() {
        DocsApiBuilder("info-success")
            .setRequest("/api/auth", HttpMethod.GET) {
                headers {
                    "Authorization" type DocsFieldType.STRING means "인증 토큰" value "Bearer ${TestConstant.TOKEN}"
                }
            }
            .setResponse {
                body {
                    "id" type DocsFieldType.STRING means "사용자의 아이디"
                    "email" type DocsFieldType.STRING means "사용자의 이메일"
                }
            }
            .execute()
            .statusCode(200)
    }

    @Test
    fun `없는 토큰을 담을시 실패한다`() {
        DocsApiBuilder("info-fail-not-valid-token")
            .setRequest("/api/auth", HttpMethod.GET) {
                headers {
                    "Authorization" type DocsFieldType.STRING means "인증 토큰" value "Bearer Invalid"
                }
            }
            .setResponse {
            }
            .execute()
            .statusCode(400)
    }

    @Test
    fun `만료된 토큰을 담을시 실패한다`() {
        DocsApiBuilder("info-fail-not-valid-token")
            .setRequest("/api/auth", HttpMethod.GET) {
                headers {
                    "Authorization" type DocsFieldType.STRING means "인증 토큰" value "Bearer ${TestConstant.EXPIRED_TOKEN}"
                }
            }
            .setResponse {
            }
            .execute()
            .statusCode(400)

    }
}
