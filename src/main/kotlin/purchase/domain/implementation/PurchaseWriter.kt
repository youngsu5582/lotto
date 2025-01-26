package purchase.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.repository.PurchaseInfoRepository
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PurchaseData

@Implementation
class PurchaseWriter(
    private val purchaseRepository: PurchaseRepository,
    private val purchaseInfoRepository: PurchaseInfoRepository,
) {
    @Transaction
    @Write
    fun savePurchase(response: PurchaseData): Purchase {
        val purchaseInfo =
            purchaseInfoRepository.save(
                PurchaseInfo(
                    totalAmount = response.totalAmount,
                    method = response.method,
                ),
            )
        return purchaseRepository.save(
            Purchase(
                paymentKey = response.paymentKey,
                orderId = response.orderId,
                status = response.status(),
                purchaseProvider = response.purchaseProvider,
                purchaseInfo = purchaseInfo,
            ),
        )
    }
}
