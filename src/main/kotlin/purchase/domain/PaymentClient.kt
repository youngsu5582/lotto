package purchase.domain

import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseRequest

interface PaymentClient {
    fun process(request: PurchaseRequest): PurchaseData
}
