package app

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import toss.TossPaymentClient
import kotlin.test.assertNotNull

@SpringBootTest
class TossClientIntegrationTest(
    @Autowired private val tossPaymentClient: TossPaymentClient
) {
    @Test
    fun `tossPaymentClient should be autowired`() {
        assertNotNull(tossPaymentClient)
    }
}
