package lotto.domain.implementation

import TestConstant
import config.ImplementationTest
import lotto.domain.entity.LottoBill
import lotto.domain.entity.LottoPublish
import lotto.domain.repository.LottoBillRepository
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseProvider
import java.math.BigDecimal
import kotlin.test.Test

@ImplementationTest
class LottoReaderTest {

    @Autowired
    private lateinit var lottoBillRepository: LottoBillRepository

    @Autowired
    private lateinit var lottoPublishRepository: LottoPublishRepository

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var purchaseRepository: PurchaseRepository

    @Autowired
    private lateinit var lottoReader: LottoReader

    /**
     *
     * 해당 부분은 사실, 예외를 던지는게 아닌 다른 VO 를 만들어서 ( owned,notOwned 와 같이 flag 를 만들어 주는게 타당한 거 같다. )
     * Reader 가 이를 판단해 예외를 던지는게 이상하다고 생각하기 때문
     */
    @Test
    fun `영수증의 멤버 ID가 일치하지 않으면 예외를 발생한다`() {
        val bill = lottoBillRepository.save(
            LottoBill(
                memberId = "memberID",
                lottoPublish = lottoPublishRepository.save(
                    LottoPublish(
                        lottoRoundInfo = lottoRoundInfoRepository.save(TestConstant.ONGOING_LOTTO_ROUND),
                        issuedAt = TestConstant.DATE_TIME,
                    )
                ),
                purchase = purchaseRepository.save(
                    Purchase(
                        paymentKey = "paymentKey",
                        orderId = "orderId",
                        status = "SUCCESS",
                        purchaseProvider = PurchaseProvider.TOSS,
                        purchaseInfo = PurchaseInfo(totalAmount = BigDecimal(1000), method = PaymentMethod.CARD)
                    )
                ),
            )
        )
        assertThrows<IllegalArgumentException> {
            lottoReader.findBill(
                bill.getId()!!,"notExist"
            )
        }
    }
}
