package purchase.domain.implementation

import config.ImplementationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import purchase.domain.vo.CancelRequest
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
        response.purchaseProvider shouldBe PurchaseProvider.TOSS
        response.isSuccess() shouldBe true
    }

    @Test
    fun `결제를 취소해서 취소 데이터를 가져온다`() {
        val response = paymentProcessor.cancel(
            cancelRequest = CancelRequest(
                amount = BigDecimal(1000),
                paymentKey = "paymentKey",
                orderId = "orderId",
                cancelReason = "단순 변심"
            ),
            PurchaseProvider.TOSS
        )
        response.purchaseProvider shouldBe PurchaseProvider.TOSS
        response.isCanceled() shouldBe true
    }
}
