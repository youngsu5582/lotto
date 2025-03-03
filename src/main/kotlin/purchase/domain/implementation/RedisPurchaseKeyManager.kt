package purchase.domain.implementation

import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

class RedisPurchaseKeyManager(
    private val redisTemplate: RedisTemplate<String, String>,
) : PurchaseKeyManager {
    override fun checkPaymentStatus(paymentKey: String): PaymentStatus {
        val key = paymentKey.getRedisKey()
        val ops = redisTemplate.opsForValue()

        val isFirstRequest = ops.setIfAbsent(key, PaymentStatus.IN_PROGRESS.name, 5, TimeUnit.MINUTES) ?: false
        if (isFirstRequest) return PaymentStatus.IN_PROGRESS
        return PaymentStatus.ALREADY_PROGRESS
    }

    override fun markAsStatus(paymentKey: String, paymentStatus: PaymentStatus) {
        val key = paymentKey.getRedisKey()
        val ops = redisTemplate.opsForValue()

        val currentStatus = ops.get(key) ?: return
        if (currentStatus == PaymentStatus.DONE.name || currentStatus == PaymentStatus.CANCELED.name) {
            throw IllegalStateException("이미 완료된 결제는 상태 변경이 불가능합니다.")
        }

        ops.set(key, paymentStatus.name, 5, TimeUnit.MINUTES)
    }

    override fun remove(paymentKey: String) {
        redisTemplate.delete(paymentKey.getRedisKey())
    }

    private fun String.getRedisKey() = "idempotent:$this"
}

enum class PaymentStatus {
    IN_PROGRESS, DONE, EMPTY, ALREADY_PROGRESS, CANCELED;

    companion object {
        fun from(name: String) = entries.find { it.name == name } ?: EMPTY
    }
}
