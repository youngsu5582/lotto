package lotto.domain

import config.FixtureLottoNumberGenerator
import config.ImplementationTest
import config.LottoRepositoryImpl
import lotto.domain.entity.IssueStatus
import lotto.domain.implementation.LottoNumberGenerator
import lotto.domain.implementation.LottoPaperGenerator
import lotto.domain.vo.LottoNumbers
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Test

@ImplementationTest
class LottoPaperGeneratorTest {

    @Autowired
    private lateinit var lottoRepository: LottoRepositoryImpl

    @Test
    fun `로또 종이를 생성한다`() {
        val generator =
            LottoPaperGenerator(
                lottoNumberGenerator = lottoNumberGenerator(),
                lottoRepository = lottoRepository,
            )

        val response =
            generator.generateWithNumbers(
                LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 34), listOf(23, 27, 39, 40, 41, 43))),
            )
        assertThat(response.toIntList()).isEqualTo(
            listOf(
                listOf(1, 14, 17, 19, 21, 34),
                listOf(23, 27, 39, 40, 41, 43),
            ),
        )
        assertThat(response.getIssuedStatues()).isEqualTo(listOf(IssueStatus.MANUAL, IssueStatus.MANUAL))
    }

//    @ParameterizedTest
//    @ValueSource(ints = [1000, 3000])
//    fun `금액보다 더 많거나,적은 요청 시 예외를 발생한다`(value: Int) {
//        val generator =
//            LottoPaperGenerator(
//                lottoNumberGenerator = lottoNumberGenerator(),
//                lottoRepository = lottoRepository,
//            )
//
//        assertThrows<IllegalArgumentException> {
//            generator.generateWithNumbers(
//                LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 34), listOf(23, 27, 39, 40, 41, 43))),
//            )
//        }
//    }

    private fun lottoNumberGenerator(numbers: List<List<Int>> = listOf()): LottoNumberGenerator {
        return FixtureLottoNumberGenerator(numbers)
    }
}
