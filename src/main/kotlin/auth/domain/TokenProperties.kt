package auth.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class TokenProperties(
    var secret: String = "",
    var accessTokenExpiry: Long = 0L,
) {
}
