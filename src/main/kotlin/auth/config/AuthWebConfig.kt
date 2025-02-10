package auth.config

import auth.service.TokenService
import member.service.MemberService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val tokenService: TokenService,
    private val memberService: MemberService
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(AccessTokenArgumentResolver())
        resolvers.add(AuthenticatedMemberArgumentResolver(tokenService, memberService))
    }
}
