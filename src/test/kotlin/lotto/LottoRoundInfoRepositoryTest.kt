package lotto

import config.RepositoryTest
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.repository.LottoRoundInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import kotlin.test.Test

@RepositoryTest
class LottoRoundInfoRepositoryTest {
    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    private val issueTime = LocalDateTime.now()
    private val endTime = issueTime.plusHours(12)
    private val drawTime = issueTime.plusDays(1)
    private lateinit var lottoRoundInfo: LottoRoundInfo

    @BeforeEach
    fun setUp() {
        lottoRoundInfo =
            lottoRoundInfoRepository.save(
                LottoRoundInfo(
                    null,
                    round = 1,
                    startDate = issueTime,
                    endDate = endTime,
                    drawDate = drawTime,
                    paymentDeadline = issueTime.plusYears(1),
                ),
            )
    }

    @Test
    fun `발행 시간보다 크거나 같으면 정보를 가져온다`() {
        val findLottoInfo =
            lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(
                issueTime,
            )
        assertThat(findLottoInfo).isEqualTo(lottoRoundInfo)
    }

    @Test
    fun `발행 시간보다 1 나노초 이전이면 가져오지 않는다`() {
        val findLottoInfo =
            lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(
                issueTime.minusNanos(1),
            )
        assertThat(findLottoInfo).isNull()
    }

    @Test
    fun `종료 시간보다 작거나 같으면 정보를 가져온다`() {
        val findLottoInfo =
            lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(
                issueTime.plusHours(12),
            )
        assertThat(findLottoInfo).isEqualTo(lottoRoundInfo)
    }

    @Test
    fun `종료 시간보다 1 나노초 이후이면 가져오지 않는다`() {
        val findLottoInfo =
            lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(
                drawTime.plusNanos(1),
            )
        assertThat(findLottoInfo).isNull()
    }
}
