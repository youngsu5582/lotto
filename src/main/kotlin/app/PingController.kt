package app

import common.dto.ApiResponse
import common.dto.apiResponse
import common.web.Get
import common.web.HttpController

@HttpController
class PingController {
    @Get("/ping")
    fun ping(): ApiResponse<String> {
        val serverName = System.getenv("SERVER_NAME") ?: "unknown"
        return apiResponse {
            data = serverName
        }
    }
}
