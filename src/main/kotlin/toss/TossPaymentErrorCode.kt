package toss

import toss.ApiGroup.CANCEL_ONLY
import toss.ApiGroup.COMMON_APIS
import toss.ApiGroup.CONFIRM_ONLY

enum class TossPaymentErrorGroup {
    NON_RETRYABLE,
    RETRYABLE
}

object ApiGroup {
    val COMMON_APIS = listOf("Confirm", "Cancel")
    val CONFIRM_ONLY = listOf("Confirm")
    val CANCEL_ONLY = listOf("Cancel")
}

enum class TossPaymentErrorCode(
    val code: String,
    val message: String,
    val group: TossPaymentErrorGroup,
    val apis: List<String>
) {

    // 공통 에러
    ALREADY_PROCESSED_PAYMENT(
        "ALREADY_PROCESSED_PAYMENT",
        "이미 처리된 결제입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        COMMON_APIS
    ),
    INVALID_REQUEST(
        "INVALID_REQUEST",
        "잘못된 요청입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        COMMON_APIS
    ),
    NOT_FOUND_PAYMENT(
        "NOT_FOUND_PAYMENT",
        "존재하지 않는 결제 정보입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        COMMON_APIS
    ),
    PROVIDER_ERROR(
        "PROVIDER_ERROR",
        "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE,
        COMMON_APIS
    ),
    FAILED_INTERNAL_SYSTEM_PROCESSING(
        "FAILED_INTERNAL_SYSTEM_PROCESSING",
        "내부 시스템 처리 작업이 실패했습니다. 잠시 후 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE,
        COMMON_APIS
    ),

    // Confirm API 전용 에러
    INVALID_REJECT_CARD(
        "INVALID_REJECT_CARD",
        "카드 사용이 거절되었습니다. 카드사 문의가 필요합니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    INVALID_STOPPED_CARD(
        "INVALID_STOPPED_CARD",
        "정지된 카드입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    EXCEED_MAX_DAILY_PAYMENT_COUNT(
        "EXCEED_MAX_DAILY_PAYMENT_COUNT",
        "하루 결제 가능 횟수를 초과했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING(
        "FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING",
        "결제가 완료되지 않았어요. 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE,
        CONFIRM_ONLY
    ),

    // Cancel API 전용 에러
    ALREADY_CANCELED_PAYMENT(
        "ALREADY_CANCELED_PAYMENT",
        "이미 취소된 결제입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_CANCELABLE_PAYMENT(
        "NOT_CANCELABLE_PAYMENT",
        "취소할 수 없는 결제입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_CANCELABLE_AMOUNT(
        "NOT_CANCELABLE_AMOUNT",
        "취소할 수 없는 금액입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    INVALID_REFUND_ACCOUNT_INFO(
        "INVALID_REFUND_ACCOUNT_INFO",
        "환불 계좌번호와 예금주명이 일치하지 않습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    INVALID_REFUND_ACCOUNT_NUMBER(
        "INVALID_REFUND_ACCOUNT_NUMBER",
        "잘못된 환불 계좌번호입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    FORBIDDEN_BANK_REFUND_REQUEST(
        "FORBIDDEN_BANK_REFUND_REQUEST",
        "고객 계좌가 입금이 되지 않는 상태입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),

    // 공통 에러 외 기본값
    UNKNOWN_PAYMENT_ERROR(
        "UNKNOWN_PAYMENT_ERROR",
        "알 수 없는 오류가 발생했습니다.",
        TossPaymentErrorGroup.RETRYABLE,
        COMMON_APIS
    );

    fun isRetryable(): Boolean {
        return group == TossPaymentErrorGroup.RETRYABLE
    }

    companion object {
        fun fromCode(code: String): TossPaymentErrorCode {
            return entries.firstOrNull { it.code == code } ?: UNKNOWN_PAYMENT_ERROR
        }
    }
}
