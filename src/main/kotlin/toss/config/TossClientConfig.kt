package toss.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestClient
import purchase.domain.PaymentClient
import toss.TossPaymentClient
import toss.TossPaymentFakeClient
import toss.TossResponseErrorHandler

@Configuration
@EnableConfigurationProperties(TossClientProperties::class)
class TossClientConfig {

    @Bean
    @Profile("prod")
    fun tossPaymentClient(tossClientProperties: TossClientProperties): PaymentClient {
        val restClient = RestClient.builder().baseUrl("https://api.tosspayments.com").build()
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

        return TossPaymentClient(
            restClient,
            tossClientProperties,
            TossResponseErrorHandler(objectMapper)
        )
    }

    @Bean
    @Profile("dev", "local", "test")
    fun tossPaymentClientFake(): PaymentClient {
        return TossPaymentFakeClient()
    }
}
