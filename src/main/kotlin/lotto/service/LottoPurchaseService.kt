package lotto.service

import auth.domain.vo.Authenticated
import common.business.BusinessService
import lotto.domain.implementation.LottoPublisher
import lotto.domain.implementation.LottoReader
import lotto.domain.implementation.LottoWriter
import lotto.domain.vo.LottoPurchaseRequest
import lotto.service.dto.LottoBillData
import lotto.service.dto.LottoPublishData
import lotto.service.dto.PurchaseData
import order.domain.implementation.OrderValidator
import purchase.domain.implementation.PaymentStatus
import purchase.domain.implementation.PurchaseKeyManager
import purchase.domain.implementation.PurchaseProcessor

@BusinessService
class LottoPurchaseService(
    private val purchaseProcessor: PurchaseProcessor,
    private val lottoPublisher: LottoPublisher,
    private val lottoWriter: LottoWriter,
    private val lottoReader: LottoReader,
    private val orderValidator: OrderValidator,
    private val purchaseKeyManager: PurchaseKeyManager
) {
    fun purchase(
        lottoPurchaseRequest: LottoPurchaseRequest,
        lottoPublishId: Long,
        authenticated: Authenticated
    ): LottoBillData {
        val paymentKey = lottoPurchaseRequest.paymentKey
        require(purchaseKeyManager.checkPaymentStatus(paymentKey) == PaymentStatus.IN_PROGRESS) {
            "결제가 이미 진행중입니다. [ 결제 ID : $paymentKey ]"
        }

        orderValidator.checkOrderValid(lottoPurchaseRequest.toOrderDataRequest())
        return runCatching {
            lottoPublisher.pending(lottoPublishId)

            val purchase = purchaseProcessor.purchase(lottoPurchaseRequest.toPurchaseRequest())
            val lottoPublish = lottoPublisher.complete(lottoPublishId)
            val bill = lottoWriter.saveBill(
                purchase.getId(),
                lottoPublishId,
                authenticated.memberId,
                lottoPublish.getLottoRoundInfo().id!!
            )

            purchaseKeyManager.markAsStatus(paymentKey, PaymentStatus.DONE)
            LottoBillData(
                id = bill.getId()!!,
                purchase = PurchaseData.from(purchase),
                lottoPublish = LottoPublishData.from(lottoPublish)
            )
        }.getOrElse { ex ->
            lottoPublisher.waiting(lottoPublishId)
            purchaseKeyManager.remove(paymentKey)
            throw ex
        }
    }

    fun cancel(
        billId: Long,
        authenticated: Authenticated
    ): LottoBillData {
        val bill = lottoReader.findBill(billId, authenticated.memberId)
        val purchase = purchaseProcessor.cancel(bill.getPurchaseId())
        val lottoPublish = lottoPublisher.unPublish(bill.getLottoPublishId())
        return LottoBillData(
            id = billId,
            purchase = PurchaseData.from(purchase),
            lottoPublish = LottoPublishData.from(lottoPublish)
        )
    }
}
