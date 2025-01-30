package purchase.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import purchase.domain.entity.Cancel
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.repository.CancelRepository
import purchase.domain.repository.PurchaseInfoRepository
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.CancelData
import purchase.domain.vo.PurchaseData

@Implementation
class PurchaseWriter(
    private val purchaseRepository: PurchaseRepository,
    private val purchaseInfoRepository: PurchaseInfoRepository,
    private val cancelRepository: CancelRepository
) {
    @Transaction
    @Write
    fun savePurchase(purchaseData: PurchaseData): Purchase {
        val purchaseInfo =
            purchaseInfoRepository.save(
                PurchaseInfo(
                    totalAmount = purchaseData.totalAmount,
                    method = purchaseData.method,
                ),
            )
        return purchaseRepository.save(
            Purchase(
                paymentKey = purchaseData.paymentKey,
                orderId = purchaseData.orderId,
                status = purchaseData.status(),
                purchaseProvider = purchaseData.purchaseProvider,
                purchaseInfo = purchaseInfo,
            ),
        )
    }

    @Transaction
    @Write
    fun cancelPurchase(cancelData: CancelData, purchase: Purchase): Purchase {
        val data = cancelData.cancels.lastOrNull() ?: throw IllegalArgumentException("취소 데이터가 존재하지 않습니다.")
        cancelRepository.save(
            Cancel(
                cancelAt = data.cancelAt,
                cancelAmount = data.cancelAmount,
                purchaseProvider = cancelData.purchaseProvider,
                orderId = cancelData.orderId,
                paymentKey = cancelData.paymentKey,
                cancelReason = data.cancelReason
            )
        )
        return purchase
    }
}
