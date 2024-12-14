package lotto.domain.vo

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LottoPaperTest {

    @Test
    fun `각 로또를 문자열 리스트로 반환한다`() {
        val lottoPaper = LottoPaper.from(
            listOf(
                listOf(1, 3, 5, 11, 15, 19),
                listOf(21, 31, 35, 40, 42, 43),
            )
        )
        Assertions.assertThat(lottoPaper.toStringListWithComma())
            .containsExactly(
                "1,3,5,11,15,19",
                "21,31,35,40,42,43",
            )
    }
}
