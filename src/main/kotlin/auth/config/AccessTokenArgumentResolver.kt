package auth.config

import auth.domain.vo.AccessToken
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AccessTokenArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == AccessToken::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): AccessToken {
        val authorization = webRequest.getHeader("Authorization") ?: throw IllegalArgumentException("토큰이 존재하지 않습니다.")
        if (!authorization.startsWith("Bearer")) {
            throw IllegalArgumentException("부적절한 토큰입니다.")
        }
        return AccessToken(authorization.substringAfter("Bearer"))
    }
}
