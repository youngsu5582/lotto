package purchase.domain.implementation

import config.ImplementationTest
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.PurchaseTemporary
import purchase.domain.repository.PurchaseTemporaryRepository
import java.math.BigDecimal

fun createPurchaseTemporary(orderId: String, amount: BigDecimal): PurchaseTemporary {
    return PurchaseTemporary(orderId = orderId, amount = amount.setScale(0))
}

@ImplementationTest
class PurchaseValidatorTest(
    private val purchaseValidator: PurchaseValidator,
    private val purchaseTemporaryRepository: PurchaseTemporaryRepository
) : FunSpec({
    extensions(listOf(SpringExtension))

    context("주문에 대한 사전 정보가 있는지 확인한다") {
        beforeTest {
            purchaseTemporaryRepository.save(createPurchaseTemporary("orderId", BigDecimal(2000)))
        }
        test("사전 정보와 일치한지 검증한다.") {
            shouldNotThrow<PurchaseException> {
                purchaseValidator.checkOrderValid(
                    OrderDataRequest("orderId", BigDecimal(2000)),
                )
            }
        }

        test("ORDER ID에 대한 주문이 없으면 예외를 발생한다.") {
            shouldThrow<PurchaseException> {
                purchaseValidator.checkOrderValid(
                    OrderDataRequest("not-exist-orderId", BigDecimal(3000)),
                )
            }.purchaseExceptionCode() shouldBe PurchaseExceptionCode.NOT_EXIST_ORDER_ID
        }

        test("주문 금액과 ORDER ID가 동일하지 않으면 예외를 발생한다.") {
            shouldThrow<PurchaseException> {
                purchaseValidator.checkOrderValid(
                    OrderDataRequest("orderId", BigDecimal(3000)),
                )
            }.purchaseExceptionCode() shouldBe PurchaseExceptionCode.NOT_VALID_ORDER_ID
        }
    }
})
