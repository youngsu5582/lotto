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

    UNKNOWN_PAYMENT_ERROR(
        "UNKNOWN_PAYMENT_ERROR",
        "알 수 없는 오류가 발생했습니다.",
        TossPaymentErrorGroup.RETRYABLE,
        COMMON_APIS
    ),

    EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT(
        "EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT",
        "즉시할인금액보다 적은 금액은 부분취소가 불가능합니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_MATCHES_REFUNDABLE_AMOUNT(
        "NOT_MATCHES_REFUNDABLE_AMOUNT",
        "잔액 결과가 일치하지 않습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    REFUND_REJECTED(
        "REFUND_REJECTED",
        "환불이 거절됐습니다. 결제사에 문의 부탁드립니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    ALREADY_REFUND_PAYMENT(
        "ALREADY_REFUND_PAYMENT",
        "이미 환불된 결제입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    INVALID_BANK(
        "INVALID_BANK",
        "유효하지 않은 은행입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    FORBIDDEN_CONSECUTIVE_REQUEST(
        "FORBIDDEN_CONSECUTIVE_REQUEST",
        "반복적인 요청은 허용되지 않습니다. 잠시 후 다시 시도해주세요.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    FORBIDDEN_REQUEST( // 결제 취소 테이블에도 동일 코드 존재
        "FORBIDDEN_REQUEST",
        "허용되지 않은 요청입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        COMMON_APIS // 승인/취소 양쪽에서 발생 가능하므로 Common
    ),
    EXCEED_MAX_REFUND_DUE(
        "EXCEED_MAX_REFUND_DUE",
        "환불 가능한 기간이 지났습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT(
        "NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT",
        "입금 대기중인 결제는 부분 환불이 불가합니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_ALLOWED_PARTIAL_REFUND(
        "NOT_ALLOWED_PARTIAL_REFUND",
        "에스크로 주문, 현금 카드 결제일 때는 부분 환불이 불가합니다...",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_AVAILABLE_BANK(
        "NOT_AVAILABLE_BANK",
        "은행 서비스 시간이 아닙니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    INCORRECT_BASIC_AUTH_FORMAT(
        "INCORRECT_BASIC_AUTH_FORMAT",
        "잘못된 요청입니다. ':' 를 포함해 인코딩해주세요.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER(
        "NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER",
        "휴면 처리된 회원의 결제는 취소할 수 없습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CANCEL_ONLY
    ),
    FAILED_REFUND_PROCESS(
        "FAILED_REFUND_PROCESS",
        "은행 응답시간 지연이나 일시적인 오류로 환불요청에 실패했습니다.",
        TossPaymentErrorGroup.RETRYABLE,
        CANCEL_ONLY
    ),
    FAILED_METHOD_HANDLING_CANCEL(
        "FAILED_METHOD_HANDLING_CANCEL",
        "취소 중 결제 시 사용한 결제 수단 처리과정에서 일시적인 오류가 발생했습니다.",
        TossPaymentErrorGroup.RETRYABLE,
        CANCEL_ONLY
    ),
    FAILED_PARTIAL_REFUND(
        "FAILED_PARTIAL_REFUND",
        "은행 점검, 해약 계좌 등의 사유로 부분 환불이 실패했습니다.",
        TossPaymentErrorGroup.RETRYABLE,
        CANCEL_ONLY
    ),
    COMMON_ERROR(
        "COMMON_ERROR",
        "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
        TossPaymentErrorGroup.RETRYABLE,
        CANCEL_ONLY
    ),

    EXCEED_MAX_CARD_INSTALLMENT_PLAN(
        "EXCEED_MAX_CARD_INSTALLMENT_PLAN",
        "설정 가능한 최대 할부 개월 수를 초과했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    NOT_ALLOWED_POINT_USE(
        "NOT_ALLOWED_POINT_USE",
        "포인트 사용이 불가한 카드로 카드 포인트 결제에 실패했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    INVALID_API_KEY(
        "INVALID_API_KEY",
        "잘못된 시크릿키 연동 정보 입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    BELOW_MINIMUM_AMOUNT(
        "BELOW_MINIMUM_AMOUNT",
        "신용카드는 결제금액이 100원 이상, 계좌는 200원이상부터 결제가 가능합니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    INVALID_CARD_EXPIRATION(
        "INVALID_CARD_EXPIRATION",
        "카드 정보를 다시 확인해주세요. (유효기간)",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT(
        "NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT",
        "할부가 지원되지 않는 카드 또는 가맹점 입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    INVALID_CARD_INSTALLMENT_PLAN(
        "INVALID_CARD_INSTALLMENT_PLAN",
        "할부 개월 정보가 잘못되었습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN(
        "NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN",
        "할부가 지원되지 않는 카드입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    EXCEED_MAX_AMOUNT(
        "EXCEED_MAX_AMOUNT",
        "거래금액 한도를 초과했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    INVALID_ACCOUNT_INFO_RE_REGISTER(
        "INVALID_ACCOUNT_INFO_RE_REGISTER",
        "유효하지 않은 계좌입니다. 계좌 재등록 후 시도해주세요.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    NOT_AVAILABLE_PAYMENT(
        "NOT_AVAILABLE_PAYMENT",
        "결제가 불가능한 시간대입니다",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    UNAPPROVED_ORDER_ID(
        "UNAPPROVED_ORDER_ID",
        "아직 승인되지 않은 주문번호입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT(
        "EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT",
        "당월 결제 가능금액인 1,000,000원을 초과 하셨습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    REJECT_ACCOUNT_PAYMENT(
        "REJECT_ACCOUNT_PAYMENT",
        "잔액부족으로 결제에 실패했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    REJECT_CARD_PAYMENT(
        "REJECT_CARD_PAYMENT",
        "한도초과 혹은 잔액부족으로 결제에 실패했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    REJECT_CARD_COMPANY(
        "REJECT_CARD_COMPANY",
        "결제 승인이 거절되었습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    REJECT_TOSSPAY_INVALID_ACCOUNT(
        "REJECT_TOSSPAY_INVALID_ACCOUNT",
        "선택하신 출금 계좌가 출금이체 등록이 되어 있지 않아요. 계좌를 다시 등록해 주세요.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    EXCEED_MAX_AUTH_COUNT(
        "EXCEED_MAX_AUTH_COUNT",
        "최대 인증 횟수를 초과했습니다. 카드사로 문의해주세요.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    EXCEED_MAX_ONE_DAY_AMOUNT(
        "EXCEED_MAX_ONE_DAY_AMOUNT",
        "일일 한도를 초과했습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    FDS_ERROR(
        "FDS_ERROR",
        "[토스페이먼츠] 위험거래가 감지되어 결제가 제한됩니다...",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),
    NOT_FOUND_PAYMENT_SESSION(
        "NOT_FOUND_PAYMENT_SESSION",
        "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
        CONFIRM_ONLY
    ),

    UNAUTHORIZED_KEY(
        "UNAUTHORIZED_KEY",
        "인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다.",
        TossPaymentErrorGroup.NON_RETRYABLE,
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
