package toss

enum class TossPaymentErrorGroup {
    NON_RETRYABLE,
    RETRYABLE
}

enum class TossPaymentErrorCode(
    val code: String,
    val message: String,
    val group: TossPaymentErrorGroup
) {
    ALREADY_PROCESSED_PAYMENT(
        "ALREADY_PROCESSED_PAYMENT",
        "이미 처리된 결제입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    INVALID_REQUEST(
        "INVALID_REQUEST",
        "잘못된 요청입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    INVALID_REJECT_CARD(
        "INVALID_REJECT_CARD",
        "카드 사용이 거절되었습니다. 카드사 문의가 필요합니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    INVALID_STOPPED_CARD(
        "INVALID_STOPPED_CARD",
        "정지된 카드입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    EXCEED_MAX_DAILY_PAYMENT_COUNT(
        "EXCEED_MAX_DAILY_PAYMENT_COUNT",
        "하루 결제 가능 횟수를 초과했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    EXCEED_MAX_PAYMENT_AMOUNT(
        "EXCEED_MAX_PAYMENT_AMOUNT",
        "하루 결제 가능 금액을 초과했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    INVALID_CARD_LOST_OR_STOLEN(
        "INVALID_CARD_LOST_OR_STOLEN",
        "분실 혹은 도난 카드입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    UNAPPROVED_ORDER_ID(
        "UNAPPROVED_ORDER_ID",
        "아직 승인되지 않은 주문번호입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    REJECT_ACCOUNT_PAYMENT(
        "REJECT_ACCOUNT_PAYMENT",
        "잔액부족으로 결제에 실패했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    NOT_FOUND_PAYMENT(
        "NOT_FOUND_PAYMENT",
        "존재하지 않는 결제 정보입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    NOT_FOUND_PAYMENT_SESSION(
        "NOT_FOUND_PAYMENT_SESSION",
        "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE
    ),
    PROVIDER_ERROR(
        "PROVIDER_ERROR",
        "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE
    ),
    CARD_PROCESSING_ERROR(
        "CARD_PROCESSING_ERROR",
        "카드사에서 오류가 발생했습니다.",
        TossPaymentErrorGroup.RETRYABLE
    ),
    FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING(
        "FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING",
        "결제가 완료되지 않았어요. 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE
    ),
    FAILED_INTERNAL_SYSTEM_PROCESSING(
        "FAILED_INTERNAL_SYSTEM_PROCESSING",
        "내부 시스템 처리 작업이 실패했습니다. 잠시 후 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE
    ),
    UNKNOWN_PAYMENT_ERROR(
        "UNKNOWN_PAYMENT_ERROR",
        "결제에 실패했어요. 같은 문제가 반복된다면 은행이나 카드사로 문의해주세요.",
        TossPaymentErrorGroup.RETRYABLE
    );

    companion object {
        fun fromCode(code: String): TossPaymentErrorCode {
            return values().firstOrNull { it.code == code } ?: UNKNOWN_PAYMENT_ERROR
        }
    }

    fun isRetryable(): Boolean {
        return group == TossPaymentErrorGroup.RETRYABLE
    }
}
