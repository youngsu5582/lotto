package app

import common.dto.ApiResponse
import common.web.Body
import common.web.Get
import common.web.HttpController
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@Profile("!prod")
@HttpController
class DocsController {
    @Get("/api/test")
    fun sampleEndpoint(
        @RequestHeader headers: Map<String, String>,
        @RequestParam params: Map<String, String>,
        @Body body: Map<String, Any>?
    ): ApiResponse<Map<String, Any>> {
        // 요청 정보를 모두 모아 응답으로 반환
        val response = mapOf(
            "headers" to headers,
            "params" to params,
            "body" to (body ?: emptyMap())
        )
        return ApiResponse.ok(response)
    }
}
