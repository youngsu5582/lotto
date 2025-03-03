package purchase.domain.implementation

import java.util.concurrent.ConcurrentHashMap

class FakePurchaseKeyManager : PurchaseKeyManager {

    private val map: MutableMap<String, PaymentStatus> = ConcurrentHashMap()

    override fun checkPaymentStatus(paymentKey: String): PaymentStatus {
        val key = paymentKey.getRedisKey()
        val previousStatus = map.putIfAbsent(key, PaymentStatus.IN_PROGRESS)
        return if (previousStatus == null) PaymentStatus.IN_PROGRESS
        else return PaymentStatus.ALREADY_PROGRESS
    }

    override fun markAsStatus(paymentKey: String, paymentStatus: PaymentStatus) {
        val key = paymentKey.getRedisKey()
        map[key] = paymentStatus
    }

    override fun remove(paymentKey: String) {
        val key = paymentKey.getRedisKey()
        map.remove(key)
    }

    // RedisPurchaseKeyManager와 동일한 key 변환 로직
    private fun String.getRedisKey() = "idempotent:$this"
}
