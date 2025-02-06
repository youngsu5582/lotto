package auth.domain

import auth.domain.vo.AccessToken
import common.business.Implementation
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

@Implementation
class TokenGenerator(
    jwtProperties: TokenProperties,
    private val clock: Clock
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(jwtProperties.secret.encodeToByteArray())
    private val accessTokenExpire: Long = jwtProperties.accessTokenExpiry

    fun generateAccessToken(value: String): AccessToken {
        require(value.isNotBlank()) { "토큰 생성은 빈 값이 될 수 없습니다" }
        val now = clock.now()
        val validity = Date(now.time.plus(accessTokenExpire))

        return AccessToken(
            Jwts.builder()
                .setSubject(value)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
        )
    }

    fun decodeToken(token: AccessToken): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.value())
                .body
        } catch (e: ExpiredJwtException) {
            throw IllegalArgumentException("만료된 토큰입니다")
        } catch (e: MalformedJwtException) {
            throw IllegalArgumentException("잘못된 형식의 토큰입니다")
        } catch (e: Exception) {
            throw IllegalArgumentException("토큰 처리 중 오류가 발생했습니다: ${e.message}")
        }
    }
}
