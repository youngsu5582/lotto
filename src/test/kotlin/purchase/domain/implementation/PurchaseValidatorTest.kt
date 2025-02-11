package purchase.domain.implementation

import config.ImplementationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseProvider
import java.math.BigDecimal

@ImplementationTest
class PurchaseValidatorTest(
    private val purchaseRepository: PurchaseRepository,
    private val purchaseValidator: PurchaseValidator,
) : FunSpec({
    extensions(listOf(SpringExtension))
    context("취소 주문에 대한 상태를 검증한다") {
        test("이미 취소된 상태라면 예외를 발생한다") {
            shouldThrow<PurchaseException> {
                val purchase = purchaseRepository.save(
                    Purchase(
                        status = "CANCELED",
                        purchaseInfo = PurchaseInfo(totalAmount = BigDecimal(1000), method = PaymentMethod.CARD),
                        purchaseProvider = PurchaseProvider.TOSS,
                        paymentKey = "paymentKey",
                        orderId = "orderId"
                    )
                )
                purchaseValidator.checkCancelValid(
                    purchase.getId().toString()
                )
            }.purchaseExceptionCode() shouldBe PurchaseExceptionCode.ALREADY_CANCELED
        }
        test("승인된 상태가 아니라면 예외를 발생한다") {
            shouldThrow<PurchaseException> {
                val purchase = purchaseRepository.save(
                    Purchase(
                        status = "READY",
                        purchaseInfo = PurchaseInfo(totalAmount = BigDecimal(1000), method = PaymentMethod.CARD),
                        purchaseProvider = PurchaseProvider.TOSS,
                        paymentKey = "paymentKey",
                        orderId = "orderId"
                    )
                )
                purchaseValidator.checkCancelValid(
                    purchase.getId().toString()
                )
            }.purchaseExceptionCode() shouldBe PurchaseExceptionCode.NOT_CONFIRMED
        }
    }
})
