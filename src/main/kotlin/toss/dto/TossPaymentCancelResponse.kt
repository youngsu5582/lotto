package toss.dto

import purchase.domain.vo.Cancel
import purchase.domain.vo.CancelData
import purchase.domain.vo.CancelStatus
import purchase.domain.vo.PurchaseProvider
import java.time.LocalDateTime

/**
 * 해당 부분은 사실 {TossPaymentConfirmResponse 와 동일하다.
 * Payment 객체이나, cancels 에 null 인지, 배열에 오는지의 차이다.
 * 당장은 분리하나, 다른 요청을 사용해 중복되면 공통화 해야 한다.
 */
data class TossPaymentCancelResponse(
    val paymentKey: String = "",
    val status: String = "",
    val orderId: String = "",
    val totalAmount: Long = 0,
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
