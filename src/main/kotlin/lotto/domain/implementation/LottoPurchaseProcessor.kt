package lotto.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.vo.PurchaseRequest
import purchase.domain.Purchase
import purchase.domain.PurchaseInfo
import purchase.domain.PurchaseProvider
import purchase.domain.repository.PurchaseInfoRepository
import purchase.domain.repository.PurchaseRepository
import toss.TossPaymentClient
import toss.dto.TossPaymentConfirmRequest
import toss.dto.TossPaymentResponse

@Implementation
class LottoPurchaseProcessor(
    private val tossPaymentClient: TossPaymentClient,
    private val purchaseRepository: PurchaseRepository,
    private val purchaseInfoRepository: PurchaseInfoRepository,
) {
    @Transaction
    @Write
    fun purchase(purchaseRequest: PurchaseRequest): Purchase {
        val response = sendPurchase(purchaseRequest)
        val purchase = savePurchase(response, PurchaseProvider.TOSS)
        return purchase
    }

    private fun savePurchase(response: TossPaymentResponse, purchaseProvider: PurchaseProvider): Purchase {
        val purchaseInfo = purchaseInfoRepository.save(
            PurchaseInfo(
                status = response.status,
                totalAmount = response.totalAmount,
                method = response.method
            )
        )
        return purchaseRepository.save(
            Purchase(
                paymentKey = response.paymentKey,
                orderId = response.orderId,
                purchaseProvider = purchaseProvider,
                purchaseInfo = purchaseInfo
            )
        )
    }

    private fun sendPurchase(purchaseRequest: PurchaseRequest): TossPaymentResponse {
        val paymentRequest = TossPaymentConfirmRequest(
            amount = purchaseRequest.amount,
            paymentKey = purchaseRequest.paymentKey,
            orderId = purchaseRequest.orderId,
        )
        return tossPaymentClient.payment(paymentRequest)
    }
}

