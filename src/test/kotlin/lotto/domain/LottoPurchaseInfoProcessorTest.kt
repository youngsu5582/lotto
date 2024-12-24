package lotto.domain

import config.ImplementationTest
import lotto.domain.implementation.LottoPurchaseProcessor
import lotto.domain.vo.Currency
import lotto.domain.vo.PurchaseType
import org.springframework.beans.factory.annotation.Autowired
import purchase.domain.vo.PurchaseRequest
import java.math.BigDecimal
import kotlin.test.Test

@ImplementationTest
class LottoPurchaseInfoProcessorTest {
    @Autowired
    private lateinit var lottoPurchaseProcessor: LottoPurchaseProcessor

    @Test
    fun `d`() {
        val response = lottoPurchaseProcessor.purchase(
            PurchaseRequest(
                amount = BigDecimal(50000),
                paymentKey = "tgen_202412170012070fF24",
                orderId = "MC42NzA0ODc4NTczNzc1",
                purchaseType = PurchaseType.CARD.name,
                currency = Currency.KRW.name
            )
        )
        println(response)
    }
}
