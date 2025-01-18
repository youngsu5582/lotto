package toss.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import toss.TossPaymentClient

@Configuration
@EnableConfigurationProperties(TossClientProperties::class)
class TossClientConfig {
    @Bean
    fun restClient(): RestClient {
        return RestClient.builder().baseUrl("https://api.tosspayments.com")
            .build()
    }

    @Bean
    fun tossPaymentClient(tossClientProperties: TossClientProperties): TossPaymentClient {
        return TossPaymentClient(restClient(), tossClientProperties)
    }
}
