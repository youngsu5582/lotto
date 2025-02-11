package auth.config

import auth.domain.vo.AccessToken
import auth.domain.vo.AuthenticatedMember
import auth.service.TokenService
import member.service.MemberService
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.server.ResponseStatusException


class AuthenticatedMemberArgumentResolver(
    private val tokenService: TokenService,
    private val memberService: MemberService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == AuthenticatedMember::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val authorization = webRequest.getHeader(AUTHORIZATION)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.")

        if (!authorization.startsWith(TOKEN_PREFIX)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "부적절한 토큰입니다.")
        }

        val token = authorization.substringAfter(TOKEN_PREFIX).trim()
        try {
            val memberId = tokenService.decodeToken(AccessToken(token))
            val memberData = memberService.readMember(memberId)
            return AuthenticatedMember(
                id = memberData.id,
                email = memberData.email
            )
        }catch (e:IllegalArgumentException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED,e.message,e)
        }
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val TOKEN_PREFIX = "Bearer"
    }
}
