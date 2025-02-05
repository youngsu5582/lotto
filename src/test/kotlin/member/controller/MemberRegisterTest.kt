package member.controller

import config.AcceptanceTest
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import sendRequest

@AcceptanceTest(["/acceptance/member.json"])
class MemberRegisterTest {
    @Test
    fun `새로운 멤버가 회원가입을 한다`() {
        val request = createRequest(
            email = "newMember@gmail.com",
            password = "password1234"
        )
        sendRequest(
            request,
            "register-success",
            commonRequestFields(),
            successResponseFields(),
            201,
            "/api/auth/register"
        )
    }

    @Test
    fun `기존에 있는 이메일로 회원가입시 실패한다`() {
        val request = createRequest(
            email = "joyson5582@gmail.com",
            password = "password1234"
        )
        sendRequest(
            request,
            "register-fail-exist-email",
            commonRequestFields(),
            errorResponseFields(),
            400,
            "/api/auth/register"
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
        fieldWithPath("data.id").type(JsonFieldType.STRING).description("생성된 멤버 ID"),
    )

    private fun errorResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
    )
}
