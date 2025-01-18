package purchase.domain

class PurchaseException : RuntimeException {
    val purchaseExceptionCode: PurchaseExceptionCode

    constructor(purchaseExceptionCode: PurchaseExceptionCode, message: String, cause: Throwable? = null)
            : super(message, cause) {
        this.purchaseExceptionCode = purchaseExceptionCode
    }

    constructor(purchaseExceptionCode: PurchaseExceptionCode, cause: Throwable? = null)
            : this(purchaseExceptionCode, purchaseExceptionCode.message, cause)

    fun purchaseExceptionCode(): PurchaseExceptionCode = purchaseExceptionCode

    fun isReadTimeOut(): Boolean = purchaseExceptionCode.isReadTimeout()
}
