package lotto.service

import common.business.BusinessService
import lotto.domain.entity.LottoBill
import lotto.domain.implementation.LottoPaperGenerator
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoPurchaseProcessor
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.LottoPaperRequest
import lotto.domain.vo.LottoPurchaseRequest

@BusinessService
class LottoPurchaseService(
    private val lottoPaperGenerator: LottoPaperGenerator,
    private val lottoPurchaseProcessor: LottoPurchaseProcessor,
    private val lottoPublisher: LottoPublisher,
) {
    fun purchase(purchaseRequest: LottoPurchaseRequest, lottoNumbers: LottoNumbers): LottoBill {
        val lottoPaper =
            lottoPaperGenerator.generateWithNumbers(LottoPaperRequest(purchaseRequest.amount), lottoNumbers)
        val purchase = lottoPurchaseProcessor.purchase(purchaseRequest.toPurchaseRequest())
        val lottoPublish = lottoPublisher.publish(lottoPaper)
        return LottoBill(
            purchase = purchase,
            lottoPublish = lottoPublish
        )
    }
}
