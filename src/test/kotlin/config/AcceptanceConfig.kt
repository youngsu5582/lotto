package config

import member.domain.implementation.PasswordEncoder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import purchase.domain.implementation.PaymentProcessor
import purchase.domain.implementation.PurchaseKeyManager
import purchase.domain.implementation.TestPaymentClient

@TestConfiguration
class AcceptanceConfig {

    @Bean
    @Primary
    fun paymentProcessor(): PaymentProcessor {
        return PaymentProcessor(
            paymentClients = listOf(
                TestPaymentClient()
            )
        )
    }

    @Bean
    @Primary
    fun passwordEncoder(): PasswordEncoder {
        return TestEncoder()
    }

    @Bean
    @Primary
    fun purchaseKeyManager(): PurchaseKeyManager {
        return FakePurchaseKeyManager(
        )
    }
}

private class TestEncoder : PasswordEncoder {
    override fun encode(plainPassword: String): String {
        return plainPassword
    }

    override fun matches(plainPassword: String, encodedPassword: String): Boolean {
        return plainPassword == encodedPassword
    }
}
