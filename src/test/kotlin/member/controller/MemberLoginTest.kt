package member.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import docs.field.means
import docs.field.value
import docs.request.DslContainer
import docs.request.body
import kotlin.test.Test

@AcceptanceTest(["/acceptance/member.json"])
class MemberLoginTest {
    @Test
    fun `로그인을 해 토큰을 받는다`() {
        DocsApiBuilder("login-success")
            .setRequestContainer("/api/auth/login", HttpMethod.POST) {
                createRequest(
                    email = "joyson5582@gmail.com",
                    password = "password1234"
                )
            }
            .setResponse {
                body {
                    "accessToken" type DocsFieldType.STRING means "생성된 액세스 토큰"
                }
            }
            .execute()
            .statusCode(200)
    }

    @Test
    fun `멤버가 없으면 예외가 발생한다`() {
        DocsApiBuilder("login-fail-not-exist-member")
            .setRequestContainer("/api/auth/login", HttpMethod.POST) {
                createRequest(
                    email = "notExist@gmail.com",
                    password = "password1234"
                )
            }
            .execute()
            .statusCode(400)
    }

    @Test
    fun `비밀번호가 틀리면 예외가 발생한다`() {
        DocsApiBuilder("login-fail-password")
            .setRequestContainer("/api/auth/login", HttpMethod.POST) {
                createRequest(
                    email = "joyson5582@gmail.com",
                    password = "notEqualPassword"
                )
            }
            .execute()
            .statusCode(400)
    }

    private fun createRequest(
        email: String,
        password: String
    ): DslContainer {
        return DslContainer().apply {
            body {
                "email" type DocsFieldType.STRING means "로그인 할 이메일" value email
                "password" type DocsFieldType.STRING means "로그인 할 비밀번호" value password
            }
        }
    }
}
