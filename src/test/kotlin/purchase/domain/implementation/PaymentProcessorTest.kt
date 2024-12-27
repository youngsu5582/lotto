package purchase.domain.implementation

import config.ImplementationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@ImplementationTest
class PaymentProcessorTest {
    @Autowired
    private lateinit var paymentProcessor: PaymentProcessor

    @Test
    fun `should process payment`() {
        println("PaymentProcessor Bean: $paymentProcessor")
    }
}
