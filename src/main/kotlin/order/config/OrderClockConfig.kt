package order.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class OrderClockConfig {
    @Bean
    fun clock(): Clock = Clock.systemDefaultZone()
}
