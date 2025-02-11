package lotto.service

import app.TestConfig
import auth.domain.vo.AuthenticatedMember
import config.ImplementationTest
import lotto.Fixture.LottoRoundFixture.createOngoingLottoRoundInfo
import lotto.domain.entity.LottoBill
import lotto.domain.entity.LottoPublish
import lotto.domain.entity.LottoPublishStatus
import lotto.domain.repository.LottoBillRepository
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import org.junit.jupiter.api.BeforeEach
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

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseServiceCancelTest {
    private lateinit var bill: LottoBill

    @Autowired
    private lateinit var lottoPublishRepository: LottoPublishRepository

    @Autowired
    private lateinit var purchaseRepository: PurchaseRepository

    @Autowired
    private lateinit var lottoBillRepository: LottoBillRepository

    @Autowired
    private lateinit var lottoPurchaseService: LottoPurchaseService

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @BeforeEach
    fun setup() {
        val publish = lottoPublishRepository.save(
            LottoPublish(
                lottoRoundInfo = lottoRoundInfoRepository.save(
                    createOngoingLottoRoundInfo()
                ),
                issuedAt = TestConstant.DATE_TIME,
                status = LottoPublishStatus.COMPLETE
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
