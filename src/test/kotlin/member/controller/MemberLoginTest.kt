package member.controller

import config.AcceptanceTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import sendRequest
import kotlin.test.Test

@AcceptanceTest(["/acceptance/member.json"])
class MemberLoginTest {
    @Test
    fun `로그인을 해 토큰을 받는다`() {
        val request = createRequest(
            email = "joyson5582@gmail.com",
            password = "password1234"
        )
        sendRequest(
            request,
            "login-success",
            commonRequestFields(),
            successResponseFields(),
            200,
            "/api/auth/login"
        )
    }

    @Test
    fun `멤버가 없으면 예외가 발생한다`() {
        val request = createRequest(
            email = "notExist@gmail.com",
            password = "password1234"
        )
        sendRequest(
            request,
            "login-fail-not-exist-member",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/auth/login"
        )
    }

    @Test
    fun `비밀번호가 틀리면 예외가 발생한다`() {
        val request = createRequest(
            email = "notExist@gmail.com",
            password = "notEqualPassword"
        )
        sendRequest(
            request,
            "login-fail-password",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/auth/login"
        )
    }

    private fun createRequest(
        email: String,
        password: String
    ): Map<String, Any> {
        return mapOf(
            "email" to email,
            "password" to password
        )
    }

    private fun commonRequestFields() = requestFields(
        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
    )

    private fun successResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("생성된 액세스 토큰"),
    )

    private fun errorResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
    )
}
