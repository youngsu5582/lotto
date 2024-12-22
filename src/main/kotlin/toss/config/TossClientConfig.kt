package toss.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import toss.TossPaymentClient

@Configuration
class TossClientConfig {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder().baseUrl("https://api.tosspayments.com").build()
    }

    @Bean
    fun tossPaymentClient(): TossPaymentClient {
        return TossPaymentClient(restClient())
    }
}
