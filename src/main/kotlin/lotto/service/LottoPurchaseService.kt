package lotto.service

import common.business.BusinessService
import lotto.domain.implementation.LottoPaperGenerator
import lotto.domain.implementation.LottoPaperRequest
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoPurchaseProcessor
import lotto.domain.entity.LottoBill
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.PurchaseRequest

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
