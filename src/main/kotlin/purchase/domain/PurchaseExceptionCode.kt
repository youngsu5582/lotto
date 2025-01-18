package purchase.domain

enum class PurchaseExceptionCode(val message: String) {
    READ_TIMEOUT("Read timed out"),
    CONNECT_TIMEOUT("Connect timed out"),
    RETRY_FAILED("Retry failed"),
    FAILED("Failed"),
    ALREADY_PROCESS("Already processed"),
    DUPLICATE_ORDER_ID("Duplicate orderId"),
    NOT_EXIST_ORDER_ID("Not exist order id"),
    NOT_VALID_ORDER_ID(" not valid order id"),
    UNKNOWN("Unknown")
    ;

    companion object {
        fun fromMessage(message: String): PurchaseExceptionCode {
            return values().firstOrNull { it.message == message }
                ?: throw IllegalArgumentException("Unknown message: $message")
        }
    }

    fun isReadTimeout(): Boolean = this == READ_TIMEOUT
}
