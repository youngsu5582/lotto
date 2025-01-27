package lotto.service

import common.business.BusinessService
import lotto.domain.entity.LottoPublish
import lotto.domain.implementation.LottoPaperGenerator
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoReader
import lotto.domain.implementation.LottoWriter
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.LottoPaperRequest
import lotto.domain.vo.LottoPurchaseRequest
import lotto.service.dto.LottoPublishData
import lotto.service.dto.LottoPurchaseData
import lotto.service.dto.PurchaseData
import purchase.domain.implementation.PurchaseProcessor
import java.time.Clock
import java.time.LocalDateTime

@BusinessService
class LottoPurchaseService(
    private val lottoPaperGenerator: LottoPaperGenerator,
    private val purchaseProcessor: PurchaseProcessor,
    private val lottoPublisher: LottoPublisher,
    private val lottoWriter: LottoWriter,
    private val clock: Clock,
    private val lottoReader: LottoReader,
) {
    fun purchase(
        lottoPurchaseRequest: LottoPurchaseRequest,
        lottoNumbers: LottoNumbers,
    ): LottoPurchaseData {
        val lottoPublish = publish(lottoPurchaseRequest, lottoNumbers)
        val purchase = purchaseProcessor.purchase(lottoPurchaseRequest.toPurchaseRequest())
        return LottoPurchaseData.from(lottoWriter.saveBill(purchase, lottoPublish))
    }

    fun cancel(
        billId: Long
    ): LottoPurchaseData {
        val bill = lottoReader.findBill(billId)
        val purchase = purchaseProcessor.cancel(bill.getPurchase())
        val lottoPublish = lottoPublisher.unPublish(bill.getLottoPublish())
        return LottoPurchaseData(
            purchase = PurchaseData.from(purchase),
            lottoPublish = LottoPublishData.from(lottoPublish)
        )
    }

    private fun publish(lottoPurchaseRequest: LottoPurchaseRequest, lottoNumbers: LottoNumbers): LottoPublish {
        val issuedAt = LocalDateTime.now(clock)
        val lottoPaper =
            lottoPaperGenerator.generateWithNumbers(LottoPaperRequest(lottoPurchaseRequest.amount), lottoNumbers)
        return lottoPublisher.publish(issuedAt, lottoPaper)
    }
}
