package member.domain.implementation

import common.business.Implementation
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Clock
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import member.config.TokenProperties
import java.util.*
import javax.crypto.SecretKey

@Implementation
class TokenGenerator(
    jwtProperties: TokenProperties,
    private val clock: Clock
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(jwtProperties.secret.encodeToByteArray())
    private val accessTokenExpire: Long = jwtProperties.accessTokenExpiry

    fun generateAccessToken(value: String): String {
        val now = clock.now()
        val validity = Date(now.time.plus(accessTokenExpire))

        return Jwts.builder()
            .setSubject(value)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun decodeToken(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token: ${e.message}")
        }
    }
}
