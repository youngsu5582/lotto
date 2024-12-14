package lotto

import lotto.domain.Lotto
import lotto.domain.repository.LottoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test

@DataJpaTest
class LottoRepositoryTest {
    @Autowired
    private lateinit var lottoRepository: LottoRepository

    private val lotto1 = lottoCreate(1, 13, 22, 25, 27, 29)
    private val lotto2 = lottoCreate(13, 24, 31, 35, 38, 41)
    private val lotto3 = lottoCreate(2, 3, 4, 5, 6, 7)

    @BeforeEach
    fun setup() {
        lottoRepository.deleteAll()
        lottoRepository.saveAll(listOf(lotto1, lotto2, lotto3))
    }

    @Test
    fun `문자열과 일치한 로또를 가져온다`() {
        val lotto = lottoRepository.findLottoByNumber("1,13,22,25,27,29")
        assertThat(lotto).isNotNull.isEqualTo(lotto1)
    }

    @Test
    fun `문자열과 일치한 로또들을 가져온다`() {
        val lottoes = lottoRepository.findLottoByNumbersIn(listOf("1,13,22,25,27,29", "13,24,31,35,38,41"))
        assertThat(lottoes).containsExactly(
            lotto1,
            lotto2
        )
    }

    @Test
    fun `숫자를 벗어난 로또는 존재하지 않는다`() {
        val lotto = lottoRepository.findLottoByNumber("1,13,22,25,27,46")
        assertThat(lotto).isNull()
    }

    private fun lottoCreate(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int): Lotto {
        return Lotto(listOf(i, i1, i2, i3, i4, i5))
    }
}
