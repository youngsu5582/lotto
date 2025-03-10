package lottoDraw.domain.vo

import io.kotest.matchers.shouldBe
import lotto.domain.vo.LottoNumber
import lotto.domain.vo.LottoNumbers
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LottoDrawTest {
    @Test
    fun `숫자와 매칭 갯수를 비교한다`() {
        val lottoDraw = LottoDraw(
            round = 10,
            date = LocalDate.of(2023, 2, 20),
            numbers = LottoNumbers.from(listOf(1, 10, 15, 18, 19, 30)),
            bonus = LottoNumber(20)
        )

        val result = lottoDraw.matchResult(LottoNumbers.from(listOf(1, 10, 15, 18, 19, 30)))
        with(result) {
            matchCount shouldBe 6
            bonusMatch shouldBe false
        }
    }

}
