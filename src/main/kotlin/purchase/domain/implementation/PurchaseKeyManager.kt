package purchase.domain.implementation

interface PurchaseKeyManager {
    fun checkPaymentStatus(paymentKey: String): PaymentStatus
    fun markAsStatus(paymentKey: String, paymentStatus: PaymentStatus)
    fun remove(paymentKey: String)
}
