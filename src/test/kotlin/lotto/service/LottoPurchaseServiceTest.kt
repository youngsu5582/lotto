package lotto.service

import app.TestConfig
import config.ImplementationTest
import lotto.domain.entity.*
import lotto.domain.repository.LottoBillRepository
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.vo.Currency
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.LottoPurchaseRequest
import lotto.domain.vo.PurchaseType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.entity.PurchaseTemporary
import purchase.domain.repository.PurchaseRepository
import purchase.domain.repository.PurchaseTemporaryRepository
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
    private lateinit var purchaseTemporaryRepository: PurchaseTemporaryRepository

    private val dateTime = LocalDateTime.now()

    @Nested
    inner class PurchaseCase {
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
            purchaseTemporaryRepository.save(
                PurchaseTemporary(
                    orderId = "orderId",
                    amount = BigDecimal(1000)
                )
            )
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
                    lottoNumbers = LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 34)))
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

    @Autowired
    private lateinit var lottoRepository: LottoRepository

    @Nested
    inner class CancelCase {
        private lateinit var bill: LottoBill

        @BeforeEach
        fun setup() {
            val publish = lottoPublishRepository.save(
                LottoPublish(
                    lottoRoundInfo = lottoRoundInfoRepository.save(
                        LottoRoundInfo(
                            null,
                            round = 1,
                            startDate = dateTime.minusHours(1),
                            endDate = dateTime.plusHours(1),
                            drawDate = dateTime.plusHours(2),
                            paymentDeadline = dateTime.plusYears(1),
                        ),
                    ),
                    issuedAt = dateTime,
                    lottoes = listOf(lottoRepository.save(Lotto(listOf(1, 5, 7, 11, 13, 15)))),
                    issuedLottoesStatus = listOf(IssueStatus.AUTO),
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
                LottoBill(lottoPublish = publish, purchase = purchase)
            )
        }

        @Test
        fun `취소가 성공적으로 진행된다`() {
            assertDoesNotThrow {
                lottoPurchaseService.cancel(
                    billId = bill.getId()!!
                )
            }
        }
    }
}
