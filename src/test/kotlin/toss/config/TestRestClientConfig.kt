package toss.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient


@Configuration
class TestRestClientConfig {
    @Bean("testRestClient")
    fun restClient(): RestClient? {
        return RestClient
            .builder()
            .requestFactory(getClientHttpRequestFactory())
            .build()
    }

    private fun getClientHttpRequestFactory(): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(1000)
        factory.setConnectTimeout(8000)
        return factory
    }
}
