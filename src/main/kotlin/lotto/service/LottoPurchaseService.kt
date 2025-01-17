package lotto.service

import common.business.BusinessService
import lotto.domain.entity.LottoPublish
import lotto.domain.implementation.LottoPaperGenerator
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoPurchaseProcessor
import lotto.domain.implementation.LottoWriter
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.LottoPaperRequest
import lotto.domain.vo.LottoPurchaseRequest
import lotto.service.dto.LottoPurchaseData
import java.time.Clock
import java.time.LocalDateTime

@BusinessService
class LottoPurchaseService(
    private val lottoPaperGenerator: LottoPaperGenerator,
    private val lottoPurchaseProcessor: LottoPurchaseProcessor,
    private val lottoPublisher: LottoPublisher,
    private val lottoWriter: LottoWriter,
    private val clock: Clock,
) {
    fun purchase(
        lottoPurchaseRequest: LottoPurchaseRequest,
        lottoNumbers: LottoNumbers,
    ): LottoPurchaseData {
        val lottoPublish = publish(lottoPurchaseRequest, lottoNumbers)
        val purchase = lottoPurchaseProcessor.purchase(lottoPurchaseRequest)
        return LottoPurchaseData.from(lottoWriter.saveBill(purchase, lottoPublish))
    }

    private fun publish(lottoPurchaseRequest: LottoPurchaseRequest, lottoNumbers: LottoNumbers): LottoPublish {
        val issuedAt = LocalDateTime.now(clock)
        val lottoPaper =
            lottoPaperGenerator.generateWithNumbers(LottoPaperRequest(lottoPurchaseRequest.amount), lottoNumbers)
        return lottoPublisher.publish(issuedAt, lottoPaper)
    }
}
