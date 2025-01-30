package toss.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import toss.TossPaymentClient
import toss.TossResponseErrorHandler

@Configuration
@EnableConfigurationProperties(TossClientProperties::class)
class TossClientConfig {
    fun restClient(): RestClient {
        return RestClient.builder().baseUrl("https://api.tosspayments.com")
            .build()
    }

    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(JavaTimeModule())
    }

    @Bean
    fun tossPaymentClient(tossClientProperties: TossClientProperties): TossPaymentClient {
        return TossPaymentClient(restClient(), tossClientProperties, TossResponseErrorHandler(objectMapper()))
    }
}
