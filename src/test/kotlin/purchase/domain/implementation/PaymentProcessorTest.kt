package purchase.domain.implementation

import config.ImplementationTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest
import java.math.BigDecimal

@ImplementationTest
class PaymentProcessorTest {
    private val paymentProcessor = PaymentProcessor(
        paymentClients = mapOf(
            PurchaseProvider.TOSS.name to TestPaymentClient()
        )
    )

    @Test
    fun `결제를 진행해서 결제 데이터를 가져온다`() {
        val response = paymentProcessor.purchase(
            purchaseRequest = PurchaseRequest(
                purchaseType = "CARD",
                currency = "KRW",
                amount = BigDecimal(1000),
                paymentKey = "paymentKey",
                orderId = "orderId"
            ),
            PurchaseProvider.TOSS
        )
        Assertions.assertThat(response.purchaseProvider).isEqualTo(PurchaseProvider.TOSS)
        Assertions.assertThat(response.status).isEqualTo("SUCCESS")
    }
}
