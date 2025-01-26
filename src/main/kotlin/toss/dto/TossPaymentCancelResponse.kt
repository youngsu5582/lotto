package toss.dto

import purchase.domain.vo.Cancel
import purchase.domain.vo.CancelData
import purchase.domain.vo.CancelStatus
import purchase.domain.vo.PurchaseProvider
import java.time.LocalDateTime

data class TossPaymentCancelResponse(
    val orderId: String = "",
    val paymentKey: String = "",
    val status: String = "",
    val cancels: Array<CancelResponse>
) {
    fun toCancelData(): CancelData {
        return CancelData(
            orderId = orderId,
            paymentKey = paymentKey,
            cancels = cancels.map { it.toCancel() },
            status = convertStatus(status),
            PurchaseProvider.TOSS
        )
    }

    private fun convertStatus(status: String): CancelStatus {
        return when (status) {
            "CANCELED" -> CancelStatus.CANCELED
            else -> CancelStatus.FAIL
        }
    }
}

data class CancelResponse(
    val transactionKey: String = "",
    val cancelAt: LocalDateTime = LocalDateTime.now(),
    val cancelAmount: Long = 0L,
    val cancelReason: String = ""
) {
    fun toCancel(): Cancel {
        return Cancel(
            transactionKey = transactionKey,
            cancelAt = cancelAt,
            cancelAmount = cancelAmount,
            cancelReason = cancelReason
        )
    }

}
