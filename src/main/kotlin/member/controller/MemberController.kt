package member.controller

import common.dto.ApiResponse
import common.dto.apiResponse
import common.web.Body
import common.web.Get
import common.web.HttpController
import common.web.Post
import auth.domain.vo.AccessToken
import member.service.MemberService
import auth.service.TokenService

@HttpController
class MemberController(
    private val memberService: MemberService,
    private val tokenService: TokenService
) {
    @Post("/api/auth/login")
    fun login(@Body localLoginHttpRequest: LocalLoginHttpRequest): ApiResponse<LocalLoginHttpResponse> {
        val memberData = memberService.readMember(localLoginHttpRequest.toIdentifier())
        val token = tokenService.createToken(memberData.id)
        return apiResponse {
            data = LocalLoginHttpResponse(accessToken = token.accessToken.value)
        }
    }

    @Post("/api/auth/register")
    fun register(@Body localRegisterHttpRequest: LocalRegisterHttpRequest): ApiResponse<LocalRegisterHttpResponse> {
        val memberData = memberService.registerMember(localRegisterHttpRequest.toIdentifier())
        return apiResponse {
            data = LocalRegisterHttpResponse(id = memberData.id)
            message = "회원가입이 완료되었습니다"
            status = 201
        }
    }

    @Get("/api/auth")
    fun info(accessToken: AccessToken): ApiResponse<MemberInfoHttpResponse> {
        val memberId = tokenService.decodeToken(accessToken)
        val memberData = memberService.readMember(memberId)
        return apiResponse {
            data = MemberInfoHttpResponse(
                id = memberData.id,
                email = memberData.email
            )
        }
    }
}
