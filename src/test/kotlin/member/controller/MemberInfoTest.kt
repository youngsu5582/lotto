package member.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import docs.field.means
import docs.field.value
import docs.request.body
import docs.request.headers
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/member.json"])
class MemberInfoTest {
    // 의도적으로 시간을 매우 늘려서 사용자의 토큰을 받아놓았다.(expire : 16000000000000000)
    private val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZmMzYWQ4Mi1lM2JhLTQ1MjAtYTk2OS0wYjRhMjkzYzlhOGIiLCJpYXQiOjE3Mzg4NTIyMzIsImV4cCI6MTYwMDE3Mzg4NTIyMzJ9.bzacSBSyi3gakhKau6-m2fZTY-VllRzq0m5Crf0-ZGs"
    // 의도적으로 시간을 매우 짧게 사용자의 토큰을 받아놓았다.(expire: 10)
    private val expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZmMzYWQ4Mi1lM2JhLTQ1MjAtYTk2OS0wYjRhMjkzYzlhOGIiLCJpYXQiOjE3Mzg4NTMxMDksImV4cCI6MTczODg1MzEwOX0.I3ej_tEDUKtDpp7C2DowJGI6EI_F3RIRPhmwUhRYMmU"
    @Test
    fun `토큰을 통해 사용자의 정보를 받는다`() {
        DocsApiBuilder("info-success")
            .setRequest("/api/auth", HttpMethod.GET) {
                headers{
                    "Authorization" type DocsFieldType.STRING means "인증 토큰" value "Bearer $token"
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
                headers{
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
                headers{
                    "Authorization" type DocsFieldType.STRING means "인증 토큰" value "Bearer $expiredToken"
                }
            }
            .setResponse {
            }
            .execute()
            .statusCode(400)
    }
}
