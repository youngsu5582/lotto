package member.domain.implementation

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class BCryptPasswordEncoderTest {
    @Test
    fun `비밀번호는 단방향이다`() {
        val encoder = BCryptPasswordEncoder()
        val text = "ThisIsPassword"
        val encoded = encoder.encode(text)
        encoder.matches(text, encoded) shouldBe true
    }
}
