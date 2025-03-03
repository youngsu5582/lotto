package purchase.domain

import purchase.domain.vo.CancelData
import purchase.domain.vo.CancelRequest
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseRequest

interface PaymentClient {
    fun process(request: PurchaseRequest): PurchaseData

    fun cancel(request: CancelRequest): CancelData

    fun support(provider: String): Boolean
}
