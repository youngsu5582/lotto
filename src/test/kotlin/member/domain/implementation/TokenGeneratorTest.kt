package member.domain.implementation

import auth.domain.TokenGenerator
import io.jsonwebtoken.Clock
import io.jsonwebtoken.impl.FixedClock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import auth.domain.TokenProperties
import auth.domain.vo.AccessToken
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.util.*

class TokenGeneratorTest : FunSpec({

    context("토큰은 대칭키로 인코딩과 디코딩시 동일하다") {
        val tokenGenerator = createTokenGenerator()

        val value = "sample"
        val token = tokenGenerator.generateAccessToken(value)

        tokenGenerator.decodeToken(token).subject shouldBe value
    }
    // Jwts 자체 Clock 은 건들수가 없다.
    // parseClaimsJws(token) 로직 내부에서 DefaultCLock 을 활용한다
    // 우리는 단지, 토큰 생성을 하는 시간만 정할 수 있다.
    context("유효기간과 현재 시간이 동일하면 예외가 발생한다") {

        val now = Date.from(Instant.now())
        val clock = FixedClock(now)
        val tokenGenerator = createTokenGenerator(expire = 10, clock = clock)

        val token = tokenGenerator.generateAccessToken("sample")
        assertThrows<IllegalArgumentException> {
            tokenGenerator.decodeToken(token)
        }
    }
})

private fun createTokenGenerator(expire: Long = 16000000000000000, clock: Clock = FixedClock()) =
    TokenGenerator(
        TokenProperties(
            secret = "justkeyfortestkeyisThisKeyWithOverall256bits",
            accessTokenExpiry = expire,
        ),
        clock = clock
    )
