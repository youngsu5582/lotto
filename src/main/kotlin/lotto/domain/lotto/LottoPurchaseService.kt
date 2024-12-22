package lotto.domain.lotto

import common.business.BusinessService
import lotto.domain.lotto.vo.LottoNumbers
import lotto.domain.lotto.vo.PurchaseRequest

@BusinessService
class LottoPurchaseService(
    private val lottoPaperGenerator: LottoPaperGenerator,
    private val lottoPurchaseProcessor: LottoPurchaseProcessor,
    private val lottoPublisher: LottoPublisher,
) {
    fun purchase(purchaseRequest: PurchaseRequest, lottoNumbers: LottoNumbers): LottoBill {
        val lottoPaper =
            lottoPaperGenerator.generateWithNumbers(LottoPaperRequest(purchaseRequest.amount), lottoNumbers)
        val purchase = lottoPurchaseProcessor.purchase(purchaseRequest)
        val lottoPublish = lottoPublisher.publish(lottoPaper)
        val lottoBill = LottoBill(
            purchase = purchase,
            lottoPublish = lottoPublish
        )
        return lottoBill
    }
}
