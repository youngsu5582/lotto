package common

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val messageConverters: List<HttpMessageConverter<*>>,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(BodyArgumentResolver(messageConverters))
    }

    override fun addReturnValueHandlers(returnValueHandlers: MutableList<HandlerMethodReturnValueHandler>) {
        returnValueHandlers.add(ApiResponseReturnValueHandler(messageConverters))
    }
}
