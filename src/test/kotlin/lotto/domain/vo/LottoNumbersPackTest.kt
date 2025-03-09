package lotto.domain.vo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LottoNumbersPackTest {
    @Test
    fun `각 로또를 문자열 리스트로 반환한다`() {
        val lotto =
            LottoNumbersPack(
                lottoNumbers = listOf(listOf(1, 3, 5, 11, 15, 19), listOf(21, 31, 35, 40, 42, 43)),
            )

        assertThat(lotto.toStringWithComma())
            .containsExactly(
                "1,3,5,11,15,19",
                "21,31,35,40,42,43",
            )
    }
}
