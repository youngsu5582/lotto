package lotto.domain

import config.MockingClock
import lotto.Fixture
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRepository
import lotto.domain.vo.LottoPaper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest
class LottoPublisherTest {
    @Autowired
    private lateinit var lottoRepository: LottoRepository

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var lottoPublishRepository: LottoPublishRepository

    private val clock = MockingClock()
    private lateinit var lottoPublisher: LottoPublisher
    private val startDate = LocalDateTime.now()
    private val endDate = startDate.plusDays(1)

    @BeforeEach
    fun setup() {
        lottoPublisher = LottoPublisher(
            lottoRepository = lottoRepository,
            lottoRoundInfoRepository = lottoRoundInfoRepository,
            lottoPublishRepository = lottoPublishRepository,
            clock = clock
        )
    }

    @Test
    fun `현재 해당하는 회차가 없으면 예외를 발생한다`() {
        prepareLottoInfo(1, startDate, endDate)
        clock.setInstant(startDate.minusNanos(1))

        assertThrows<NoSuchElementException> {
            publishLottoPaper()
        }
    }

    @Test
    fun `현재 해당하는 회차가 없으면 예외를 발생한다2`() {
        prepareLottoInfo(1, startDate, endDate)
        clock.setInstant(endDate.plusNanos(1))

        assertThrows<NoSuchElementException> {
            publishLottoPaper()
        }
    }

    @Test
    fun `조회한 회차가 모집중이 아니면 예외를 발생한다`() {
        prepareLottoInfo(2, startDate, endDate, LottoStatus.CLOSED)
        clock.setInstant(startDate.plusNanos(1))

        assertThrows<IllegalArgumentException> {
            publishLottoPaper()
        }
    }

    private fun prepareLottoInfo(
        round: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: LottoStatus = LottoStatus.ONGOING
    ) {
        lottoRoundInfoRepository.save(
            Fixture.LottoInfoFixture.create(
                round = round,
                startDate = startDate,
                endDate = endDate,
                status = status
            )
        )
    }

    private fun publishLottoPaper() {
        lottoPublisher.publish(
            LottoPaper(
                listOf(
                    Lotto(listOf(1, 3, 5, 7, 9, 11)),
                    Lotto(listOf(1, 3, 5, 11, 13, 15))
                )
            )
        )
    }
}
