package purchase.domain.implementation

import common.business.Implementation
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

@Implementation
class RedisPurchaseKeyManager(
    private val redisTemplate: RedisTemplate<String, String>,
) : PurchaseKeyManager {
    override fun checkPaymentStatus(paymentKey: String): PaymentStatus {
        val key = paymentKey.getRedisKey()
        val ops = redisTemplate.opsForValue()
        val currentStatus = ops.get(key) ?: ""

        if (currentStatus.isNotEmpty()) {
            return PaymentStatus.from(currentStatus)
        }
        val isFirstRequest = ops.setIfAbsent(key, PaymentStatus.IN_PROGRESS.name, 5, TimeUnit.MINUTES) ?: false
        if (isFirstRequest) return PaymentStatus.IN_PROGRESS
        return PaymentStatus.ALREADY_PROGRESS
    }

    override fun markAsStatus(paymentKey: String, paymentStatus: PaymentStatus) {
        val ops = redisTemplate.opsForValue()
        ops.set(paymentKey.getRedisKey(), paymentStatus.name, 5, TimeUnit.MINUTES)
    }

    override fun remove(paymentKey: String) {
        redisTemplate.delete(paymentKey)
    }

    private fun String.getRedisKey() = "idempotent:$this"
}

enum class PaymentStatus {
    IN_PROGRESS, DONE, EMPTY, ALREADY_PROGRESS, CANCELED;

    companion object {
        fun from(name: String) = entries.find { it.name == name } ?: EMPTY
    }
}
