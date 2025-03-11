package lottoDraw.domain.repository

import config.RepositoryTest
import io.kotest.matchers.collections.shouldHaveSize
import lottoDraw.domain.entity.LottoNumberCount
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@RepositoryTest
class LottoNumberCountRepositoryTest {

    @Autowired
    private lateinit var lottoNumberCountRepository: LottoNumberCountRepository

    @BeforeEach
    fun setUp() {
        lottoNumberCountRepository.saveAll(
            listOf(
                LottoNumberCount(
                    number = 10,
                    count = 17,
                ),
                LottoNumberCount(
                    number = 13,
                    count = 20,
                ),
                LottoNumberCount(
                    number = 20,
                    count = 30,
                ),
            )
        )
    }

    @Test
    fun `숫자와 일치한 값을 가져온다`() {
        val results = lottoNumberCountRepository.findByNumberIn(
            listOf(10, 20)
        )
        results shouldHaveSize 2
    }
}
