package member.controller

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import docs.field.means
import docs.field.value
import docs.request.DslContainer
import docs.request.body
import org.junit.jupiter.api.Test

@AcceptanceTest(["/acceptance/member.json"])
class MemberRegisterTest {
    @Test
    fun `새로운 멤버가 회원가입을 한다`() {
        DocsApiBuilder("register-success")
            .setRequestContainer("/api/auth/register", HttpMethod.POST) {
                createRequest(
                    email = "newMember@gmail.com",
                    password = "password1234"
                )
            }
            .setResponse {
                body {
                    "id" type DocsFieldType.STRING means "생성된 멤버 아이디"
                }
            }
            .execute()
            .statusCode(201)
    }

    @Test
    fun `기존에 있는 이메일로 회원가입시 실패한다`() {
        DocsApiBuilder("register-fail-exist-email")
            .setRequestContainer("/api/auth/register", HttpMethod.POST) {
                createRequest(
                    email = "joyson5582@gmail.com",
                    password = "password1234"
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
                "email" type DocsFieldType.STRING means "회원 가입할 이메일" value email
                "password" type DocsFieldType.STRING means "회원 가입할 비밀번호" value password
            }
        }
    }
}
