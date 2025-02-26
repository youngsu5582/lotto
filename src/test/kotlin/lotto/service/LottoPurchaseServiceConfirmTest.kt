package lotto.service

import ConcurrentTestUtil
import app.TestConfig
import auth.domain.vo.AuthenticatedMember
import config.ImplementationTest
import io.kotest.matchers.shouldBe
import lotto.Fixture.LottoRoundFixture.createOngoingLottoRoundInfo
import lotto.domain.entity.LottoPublish
import lotto.domain.entity.LottoPublishStatus
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.vo.Currency
import lotto.domain.vo.LottoPurchaseRequest
import lotto.domain.vo.PurchaseType
import order.domain.entity.Order
import order.domain.repository.OrderRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDateTime

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseServiceConfirmTest {

    @Autowired
    private lateinit var lottoPurchaseService: LottoPurchaseService

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var lottoPublishRepository: LottoPublishRepository

    private var lottoPublishId: Long = 0

    @Nested
    inner class PurchaseCase {
        @BeforeEach
        fun setUp() {
            val roundInfo = lottoRoundInfoRepository.save(
                createOngoingLottoRoundInfo()
            )
            orderRepository.save(
                Order(
                    orderId = "orderId",
                    amount = BigDecimal(1000)
                )
            )
            lottoPublishId = lottoPublishRepository.save(
                LottoPublish(
                    lottoRoundInfo = roundInfo,
                    issuedAt = LocalDateTime.now(),
                    status = LottoPublishStatus.WAITING
                )
            ).getId()
        }

        @Test
        fun `결제가 성공적으로 진행된다`() {
            assertDoesNotThrow {
                lottoPurchaseService.purchase(
                    lottoPurchaseRequest = LottoPurchaseRequest(
                        purchaseType = PurchaseType.CARD,
                        currency = Currency.KRW,
                        amount = BigDecimal(1000),
                        paymentKey = "paymentKey",
                        orderId = "orderId"
                    ),
                    lottoPublishId = lottoPublishId,
                    authenticated = AuthenticatedMember("ID", "user@email.com")
                )
            }
        }

        @Test
        fun `동시에 여러개의 요청이 오면 하나만 성공한다`() {
            val results = ConcurrentTestUtil.concurrent(numberOfRequests = 10, wait = 1000L) {
                lottoPurchaseService.purchase(
                    lottoPurchaseRequest = LottoPurchaseRequest(
                        purchaseType = PurchaseType.CARD,
                        currency = Currency.KRW,
                        amount = BigDecimal(1000),
                        paymentKey = "paymentKey",
                        orderId = "orderId"
                    ),
                    lottoPublishId = lottoPublishId,
                    authenticated = AuthenticatedMember("ID", "user@email.com")
                )
            }
            results.successCount() shouldBe 1
            results.failureCount() shouldBe 9
        }
    }
}
