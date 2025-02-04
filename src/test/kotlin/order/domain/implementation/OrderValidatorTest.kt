package order.domain.implementation

import app.TestConfig
import config.ImplementationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import order.domain.repository.OrderRepository
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.springframework.context.annotation.Import
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.Order
import java.math.BigDecimal

@ImplementationTest
@Import(TestConfig::class)
class OrderValidatorTest(
    private val orderRepository: OrderRepository,
    private val orderValidator: OrderValidator
) : FunSpec({
    extensions(listOf(SpringExtension))
    context("주문에 대한 사전 정보가 있는지 확인한다") {
        val orderId = "orderId"
        val amount = BigDecimal(1000)
        beforeTest {
            orderRepository.save(
                Order(
                    orderId = orderId,
                    amount = amount
                )
            )
        }
        test("사전 정보와 일치한지 검증한다.") {
            assertDoesNotThrow {
                orderValidator.checkOrderValid(
                    OrderDataRequest(
                        orderId = orderId,
                        amount = amount
                    )
                )
            }
        }

        test("orderId 에 해당하는 값이 없으면 예외를 발생한다.") {
            val exception = shouldThrow<PurchaseException> {
                orderValidator.checkOrderValid(
                    OrderDataRequest(
                        orderId = "not Exist",
                        amount = amount
                    )
                )
            }
            exception.purchaseExceptionCode shouldBe PurchaseExceptionCode.NOT_EXIST_ORDER_ID
        }

        test("Id 에 해당하는 금액이 다르면 예외를 발생한다.") {
            val exception = shouldThrow<PurchaseException> {
                orderValidator.checkOrderValid(
                    OrderDataRequest(
                        orderId = orderId,
                        amount = BigDecimal(2000)
                    )
                )
            }
            exception.purchaseExceptionCode shouldBe PurchaseExceptionCode.NOT_VALID_ORDER_ID
        }
    }
})
