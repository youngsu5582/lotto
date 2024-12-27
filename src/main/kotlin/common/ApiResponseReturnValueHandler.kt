package common

import common.dto.ApiResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.method.support.ModelAndViewContainer

class ApiResponseReturnValueHandler(
    private val messageConverters: List<HttpMessageConverter<*>>,
) : HandlerMethodReturnValueHandler {
    override fun supportsReturnType(returnType: MethodParameter): Boolean =
        ApiResponse::class.java.isAssignableFrom(returnType.parameterType)

    override fun handleReturnValue(
        returnValue: Any?,
        returnType: MethodParameter,
        mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest,
    ) {
        val apiResponse =
            returnValue as? ApiResponse<*>
                ?: throw IllegalArgumentException("Return value is not of type ApiResponse")

        val nativeResponse =
            webRequest.getNativeResponse(HttpServletResponse::class.java)
                ?: throw IllegalStateException("Native response is missing")

        val outputMessage = ServletServerHttpResponse(nativeResponse)
        val selectedMediaType = outputMessage.headers.contentType ?: MediaType.APPLICATION_JSON

        apiResponse.headers.forEach { (key, value) ->
            outputMessage.headers.add(key, value)
        }

        val converter =
            messageConverters.find {
                it.canWrite(ApiResponse::class.java, selectedMediaType)
            } ?: throw IllegalArgumentException("No suitable HttpMessageConverter found for $selectedMediaType")

        @Suppress("UNCHECKED_CAST")
        (converter as HttpMessageConverter<ApiResponse<*>>).write(apiResponse, selectedMediaType, outputMessage)
        mavContainer.isRequestHandled = true
    }
}
