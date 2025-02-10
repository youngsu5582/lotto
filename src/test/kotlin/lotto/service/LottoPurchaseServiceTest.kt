package lotto.service

import TestConstant
import app.TestConfig
import auth.domain.vo.AuthenticatedMember
import config.ImplementationTest
import lotto.domain.entity.LottoBill
import lotto.domain.entity.LottoPublish
import lotto.domain.repository.LottoBillRepository
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
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseProvider
import java.math.BigDecimal
import java.time.LocalDateTime

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseServiceTest {

    @Autowired
    private lateinit var lottoPurchaseService: LottoPurchaseService

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    private var lottoPublishId: Long = 0

    @Nested
    inner class PurchaseCase {
        @BeforeEach
        fun setUp() {
            val roundInfo = lottoRoundInfoRepository.save(
                TestConstant.ONGOING_LOTTO_ROUND
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
    }

    @Autowired
    private lateinit var lottoPublishRepository: LottoPublishRepository

    @Autowired
    private lateinit var purchaseRepository: PurchaseRepository

    @Autowired
    private lateinit var lottoBillRepository: LottoBillRepository

    @Nested
    inner class CancelCase {
        private lateinit var bill: LottoBill

        @BeforeEach
        fun setup() {
            val publish = lottoPublishRepository.save(
                LottoPublish(
                    lottoRoundInfo = lottoRoundInfoRepository.save(
                        TestConstant.ONGOING_LOTTO_ROUND
                    ),
                    issuedAt = TestConstant.DATE_TIME,
                )
            )
            val purchase = purchaseRepository.save(
                Purchase(
                    paymentKey = "paymentKey",
                    orderId = "orderId",
                    status = "SUCCESS",
                    purchaseProvider = PurchaseProvider.TOSS,
                    purchaseInfo = PurchaseInfo(totalAmount = BigDecimal(1000), method = PaymentMethod.CARD)
                )
            )
            bill = lottoBillRepository.save(
                LottoBill(lottoPublish = publish, purchase = purchase, memberId = "ID")
            )
        }

        @Test
        fun `취소가 성공적으로 진행된다`() {
            assertDoesNotThrow {
                lottoPurchaseService.cancel(
                    billId = bill.getId()!!,
                    authenticated = AuthenticatedMember("ID", "user@email.com")
                )
            }
        }
    }
}
