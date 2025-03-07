package fixture

import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseProvider

object PurchaseFixture {
    fun 토스_제공자와_카드로_성공한_결제(
        amount: Int,
        orderId: String = "orderId",
        paymentKey: String = "paymentKey"
    ): Purchase {
        return Purchase(
            orderId = orderId,
            paymentKey = paymentKey,
            purchaseInfo = PurchaseInfo(
                totalAmount = amount.toBigDecimal(),
                method = PaymentMethod.CARD
            ),
            status = "SUCCESS",
            purchaseProvider = PurchaseProvider.TOSS
        )
    }
}
