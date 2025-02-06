package auth.config

import io.jsonwebtoken.Clock
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenConfig {
    @Bean
    fun tokenClock(): Clock {
        return DefaultClock.INSTANCE
    }
}
