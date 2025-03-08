package lotto.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class LottoWebConfig {

    @Bean
    fun corsFilter(environment: Environment): CorsFilter {
        val config = CorsConfiguration()
        val allowedOrigins = listOf(
            "http://localhost:3000",
            "http://front-lotto:3000",
            "https://www.lotto.web.youngsu5582.life",
            "https://lotto.web.youngsu5582.life",
            "https://main.d126n5xa59cq7m.amplifyapp.com"
        )
        config.allowedOrigins = allowedOrigins
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("Authorization", "Content-Type", "Accept")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config) // 모든 엔드포인트에 적용
        return CorsFilter(source)
    }
}
