package lottoDraw.domain.repository

import config.RepositoryTest
import fixture.LottoDrawFixture
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import lottoDraw.domain.entity.LottoDraw
import lottoDraw.domain.vo.LottoResult
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Test

@RepositoryTest
class LottoDrawRepositoryTest {

    @Autowired
    private lateinit var lottoDrawRepository: LottoDrawRepository

    @BeforeEach
    fun setUp() {
        lottoDrawRepository.saveAll(
            listOf(
                // 1등
                LottoDrawFixture.회차_숫자들(10, listOf(2, 7, 11, 12, 15, 18, 21)),
                // 2등
                LottoDrawFixture.회차_숫자들(11, listOf(2, 7, 11, 12, 15, 17, 18)),

                // 3등
                LottoDrawFixture.회차_숫자들(12, listOf(2, 7, 11, 12, 15, 20, 24)),
                // 4등
                LottoDrawFixture.회차_숫자들(13, listOf(2, 7, 11, 12, 20, 24, 25)),
            )
        )
    }


    @Test
    fun `일차한 값이 5개 이상인 결과만 가져온다`() {
        val results = lottoDrawRepository.findAllWithFiveOrMoreMatches(
            listOf(2, 7, 11, 12, 15, 18)
        )
        results shouldHaveSize 3
        results shouldContain  LottoResult(10,6,false)
        results shouldContain  LottoResult(11,5,true)
        results shouldContain  LottoResult(12,5,false)
    }
}
