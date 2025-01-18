package common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CustomWebConfig(
    private val messageConverters: List<HttpMessageConverter<*>>,
) : WebMvcConfigurer {
    @Bean
    fun apiResponseReturnValueHandler(): ApiResponseReturnValueHandler {
        return ApiResponseReturnValueHandler(messageConverters)
    }

    @Bean
    fun bodyArgumentResolver(): BodyArgumentResolver {
        return BodyArgumentResolver(messageConverters)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(BodyArgumentResolver(messageConverters))
    }

    override fun addReturnValueHandlers(returnValueHandlers: MutableList<HandlerMethodReturnValueHandler>) {
        returnValueHandlers.add(ApiResponseReturnValueHandler(messageConverters))
    }
}
