package purchase.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import purchase.domain.implementation.FakePurchaseKeyManager
import purchase.domain.implementation.PurchaseKeyManager
import purchase.domain.implementation.RedisPurchaseKeyManager

@Configuration
class PurchaseConfig {

    @Profile("local", "test")
    @Bean
    fun fakePurchaseKeyManager(): PurchaseKeyManager {
        return FakePurchaseKeyManager()
    }

    @Profile("dev","prod")
    @Bean
    fun purchaseKeyManager(
        redisTemplate: RedisTemplate<String, String>
    ): PurchaseKeyManager {
        return RedisPurchaseKeyManager(redisTemplate)
    }
}
