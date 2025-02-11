package lotto.service

import auth.domain.vo.Authenticated
import common.business.BusinessService
import lotto.domain.entity.LottoPublish
import lotto.domain.entity.LottoPublishStatus
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoReader
import lotto.domain.implementation.LottoWriter
import lotto.domain.vo.LottoPurchaseRequest
import lotto.service.dto.LottoBillData
import lotto.service.dto.LottoPublishData
import lotto.service.dto.PurchaseData
import order.domain.implementation.OrderValidator
import purchase.domain.implementation.PurchaseProcessor
import java.util.concurrent.ConcurrentHashMap

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
        lottoPublishId: Long,
        authenticated: Authenticated
    ): LottoBillData {
        orderValidator.checkOrderValid(lottoPurchaseRequest.toOrderDataRequest())
        val purchase = purchaseProcessor.purchase(lottoPurchaseRequest.toPurchaseRequest())
        val lottoPublish = lottoPublisher.complete(lottoPublishId)
        return LottoBillData.from(lottoWriter.saveBill(purchase.getId(), lottoPublish.getId(), authenticated.memberId))
    }

    fun cancel(
        billId: Long,
        authenticated: Authenticated
    ): LottoBillData {
        val bill = lottoReader.findBill(billId, authenticated.memberId)
        val purchase = purchaseProcessor.cancel(bill.getPurchase())
        val lottoPublish = lottoPublisher.unPublish(bill.getLottoPublish().getId())
        return LottoBillData(
            id = billId,
            purchase = PurchaseData.from(purchase),
            lottoPublish = LottoPublishData.from(lottoPublish)
        )
    }

    private fun getPublishWithStatus(lottoPublishId: Long, status: LottoPublishStatus): LottoPublish =
        lottoPublisher.findPublish(lottoPublishId).takeIf { it.isStatus(status) }
            ?: throw IllegalStateException("$lottoPublishId 에 대한 상태가 $status 가 아닙니다")
}
