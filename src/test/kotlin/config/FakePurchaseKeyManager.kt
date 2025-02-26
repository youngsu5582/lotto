package config

import purchase.domain.implementation.PaymentStatus
import purchase.domain.implementation.PurchaseKeyManager
import java.util.concurrent.ConcurrentHashMap

class FakePurchaseKeyManager : PurchaseKeyManager {

    private val map: MutableMap<String, PaymentStatus> = ConcurrentHashMap()

    override fun checkPaymentStatus(paymentKey: String): PaymentStatus {
        val key = paymentKey.getRedisKey()
        val currentStatus = map[key]
        if (currentStatus != null) {
            return currentStatus
        }
        val previous = map.putIfAbsent(key, PaymentStatus.IN_PROGRESS)
        return if (previous == null) PaymentStatus.ALREADY_PROGRESS else PaymentStatus.IN_PROGRESS
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
