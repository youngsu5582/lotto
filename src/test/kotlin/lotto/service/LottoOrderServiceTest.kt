package lotto.service

import app.TestConfig
import config.ImplementationTest
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.vo.LottoNumbers
import order.domain.repository.OrderRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import java.time.LocalDateTime

@ImplementationTest
@Import(TestConfig::class)
class LottoOrderServiceTest {
    @Autowired
    private lateinit var lottoOrderService: LottoOrderService

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var lottoPublishRepository: LottoPublishRepository

    @Nested
    inner class OrderCase {
        private val dateTime = LocalDateTime.now()

        @BeforeEach
        fun setUp() {
            lottoRoundInfoRepository.save(
                LottoRoundInfo(
                    null,
                    round = 1,
                    startDate = dateTime.minusHours(1),
                    endDate = dateTime.plusHours(1),
                    drawDate = dateTime.plusHours(2),
                    paymentDeadline = dateTime.plusYears(1),
                ),
            )
        }

        @Test
        fun `주문이 성공적으로 진행된다`() {
            assertDoesNotThrow {
                lottoOrderService.saveLottoOrder(
                    lottoNumbers = LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 23)))
                )
            }
        }

        @Test
        fun `모든 컴포넌트가 정상적으로 상호작용하여 주문을 처리한다`() {
            // Given
            val lottoNumbers = LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 23)))

            // When
            val result = lottoOrderService.saveLottoOrder(lottoNumbers)

            // Then
            val savedOrder = orderRepository.findByOrderId(result.orderData.orderId)
            assertTrue(savedOrder.isPresent)
            assertEquals(result.orderData.amount, savedOrder.get().getAmount().setScale(0))

            val lottoPublish = lottoPublishRepository.findById(result.lottoPublishId)
            assertTrue(lottoPublish.isPresent)
        }
    }
}
