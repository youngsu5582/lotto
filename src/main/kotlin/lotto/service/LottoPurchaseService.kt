package lotto.service

import common.business.BusinessService
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoReader
import lotto.domain.implementation.LottoWriter
import lotto.domain.vo.LottoPurchaseRequest
import lotto.service.dto.LottoPublishData
import lotto.service.dto.LottoPurchaseData
import lotto.service.dto.PurchaseData
import order.domain.implementation.OrderValidator
import purchase.domain.implementation.PurchaseProcessor

@BusinessService
class LottoPurchaseService(
    private val purchaseProcessor: PurchaseProcessor,
    private val lottoPublisher: LottoPublisher,
    private val lottoWriter: LottoWriter,
    private val lottoReader: LottoReader,
    private val orderValidator: OrderValidator,
) {
    fun purchase(
        lottoPurchaseRequest: LottoPurchaseRequest,
        lottoPublishId: Long
    ): LottoPurchaseData {
        orderValidator.checkOrderValid(lottoPurchaseRequest.toOrderDataRequest())
        val lottoPublish = lottoPublisher.findPublish(lottoPublishId)
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
}
