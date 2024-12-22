package lotto.domain

import config.FixtureLottoNumberGenerator
import lotto.domain.lotto.IssueStatus
import lotto.domain.lotto.LottoNumberGenerator
import lotto.domain.lotto.LottoPaperGenerator
import lotto.domain.lotto.LottoPaperRequest
import lotto.domain.lotto.vo.LottoNumbers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import kotlin.test.Test

class LottoPaperGeneratorTest {
    @Test
    fun `금액에 맞게 자동으로 숫자를 생성한다1`() {
        val generator = LottoPaperGenerator(
            lottoNumberGenerator = lottoNumberGenerator(
                listOf(
                    listOf(1, 14, 17, 19, 21, 34)
                )
            )
        )
        val response = generator.generateWithAuto(
            lottoPaperRequest = LottoPaperRequest(BigDecimal(1000))
        )

        assertThat(response.toIntList()).isEqualTo(listOf(listOf(1, 14, 17, 19, 21, 34)))
        assertThat(response.getIssuedStatues()).isEqualTo(listOf(IssueStatus.AUTO))
    }

    @Test
    fun `금액에 맞게 자동으로 숫자를 생성한다2`() {
        val generator = LottoPaperGenerator(
            lottoNumberGenerator = lottoNumberGenerator(
                listOf(
                    listOf(1, 14, 17, 19, 21, 34), listOf(23, 27, 39, 40, 41, 43)
                )
            )
        )

        val response = generator.generateWithAuto(
            lottoPaperRequest = LottoPaperRequest(BigDecimal(2000))
        )
        assertThat(response.toIntList()).isEqualTo(
            listOf(
                listOf(1, 14, 17, 19, 21, 34),
                listOf(23, 27, 39, 40, 41, 43)
            )
        )
        assertThat(response.getIssuedStatues()).isEqualTo(listOf(IssueStatus.AUTO, IssueStatus.AUTO))
    }

    @Test
    fun `금액이 기준에 맞지 않으면 예외를 발생한다`() {
        val generator = LottoPaperGenerator(
            lottoNumberGenerator = lottoNumberGenerator(
                listOf(
                    listOf(1, 14, 17, 19, 21, 34)
                )
            )
        )
        assertThrows<IllegalArgumentException> {
            generator.generateWithAuto(
                lottoPaperRequest = LottoPaperRequest(BigDecimal(1500))
            )
        }
    }

    @Test
    fun `금액에 맞게 수동으로 생성한다`() {
        val generator = LottoPaperGenerator(
            lottoNumberGenerator = lottoNumberGenerator()
        )

        val response = generator.generateWithNumbers(
            lottoPaperRequest = LottoPaperRequest(BigDecimal(2000)),
            LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 34), listOf(23, 27, 39, 40, 41, 43)))
        )
        assertThat(response.toIntList()).isEqualTo(
            listOf(
                listOf(1, 14, 17, 19, 21, 34),
                listOf(23, 27, 39, 40, 41, 43)
            )
        )
        assertThat(response.getIssuedStatues()).isEqualTo(listOf(IssueStatus.MANUAL, IssueStatus.MANUAL))
    }

    @ParameterizedTest
    @ValueSource(ints = [1000, 3000])
    fun `금액보다 더 많거나,적은 요청 시 예외를 발생한다`(value: Int) {
        val generator = LottoPaperGenerator(
            lottoNumberGenerator = lottoNumberGenerator()
        )

        assertThrows<IllegalArgumentException> {
            generator.generateWithNumbers(
                lottoPaperRequest = LottoPaperRequest(BigDecimal(value)),
                LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 34), listOf(23, 27, 39, 40, 41, 43)))
            )
        }
    }

    private fun lottoNumberGenerator(
        numbers: List<List<Int>> = listOf()
    ): LottoNumberGenerator {
        return FixtureLottoNumberGenerator(numbers)
    }
}
