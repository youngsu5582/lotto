package toss.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "toss.client")
data class TossClientProperties(
    var apiKey: String = "",
    var paymentUrl: String = "",
    var cancelUrl:String = "",
) {
}
