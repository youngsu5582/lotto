package app

import config.FixtureLottoNumberGenerator
import config.LottoRepositoryImpl
import config.MockingClock
import lotto.domain.implementation.LottoNumberGenerator
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import purchase.domain.implementation.PaymentProcessor
import purchase.domain.implementation.TestPaymentClient

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun lottoRepositoryImpl(): LottoRepositoryImpl {
        return LottoRepositoryImpl()
    }

    @Bean
    @Primary
    fun mockingClock(): MockingClock {
        return MockingClock()
    }

    @Bean
    @Primary
    fun fixtureLottoNumberGenerator(): LottoNumberGenerator {
        return FixtureLottoNumberGenerator(listOf(listOf(1, 2, 3, 4, 5, 6)))
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
