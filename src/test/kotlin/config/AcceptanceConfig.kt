package config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import purchase.domain.implementation.PaymentProcessor
import purchase.domain.implementation.TestPaymentClient

@TestConfiguration
class AcceptanceConfig {
    @Bean
    @Primary
    fun lottoRepositoryImpl(): LottoRepositoryImpl {
        return LottoRepositoryImpl()
    }

    @Bean
    @Primary
    fun paymentProcessor(): PaymentProcessor {
        return PaymentProcessor(
            paymentClients = mapOf(
                "TOSS" to TestPaymentClient()
            )
        )
    }
}
