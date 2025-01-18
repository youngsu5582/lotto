package lotto.domain.implementation

import app.TestConfig
import config.ImplementationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import lotto.domain.vo.Currency
import lotto.domain.vo.LottoPurchaseRequest
import lotto.domain.vo.PurchaseType
import org.springframework.context.annotation.Import
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.implementation.OrderDataProcessor
import purchase.domain.implementation.OrderDataRequest
import java.math.BigDecimal

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseProcessorTest(
    private val lottoPurchaseProcessor: LottoPurchaseProcessor,
    private val orderDataProcessor: OrderDataProcessor
) : FunSpec({
    val orderId = "orderId"
    val paymentKey = "paymentKey"
    val amount = BigDecimal(1000)
    extensions(listOf(SpringExtension))

    context("주문에 대한 사전 정보가 있는지 확인한다") {
        beforeTest {
            orderDataProcessor.saveTemporary(
                request = OrderDataRequest(
                    orderId = orderId,
                    amount = amount
                )
            )
        }
        test("사전 정보와 일치한지 검증한다.") {
            val result = lottoPurchaseProcessor.purchase(
                LottoPurchaseRequest(
                    purchaseType = PurchaseType.CARD,
                    amount = BigDecimal(1000),
                    orderId = orderId,
                    paymentKey = paymentKey,
                    currency = Currency.KRW
                )
            )
            result.isSuccess() shouldBe true
            result.getId() shouldNotBe null
        }

        test("orderId 에 해당하는 값이 없으면 예외를 발생한다.") {
            val exception = shouldThrow<PurchaseException> {
                lottoPurchaseProcessor.purchase(
                    LottoPurchaseRequest(
                        purchaseType = PurchaseType.CARD,
                        amount = amount,
                        orderId = "not Exist",
                        paymentKey = paymentKey,
                        currency = Currency.KRW
                    )
                )
            }
            exception.purchaseExceptionCode shouldBe PurchaseExceptionCode.NOT_EXIST_ORDER_ID
        }

        test("Id 에 해당하는 금액이 다르면 예외를 발생한다.") {
            val exception = shouldThrow<PurchaseException> {
                lottoPurchaseProcessor.purchase(
                    LottoPurchaseRequest(
                        purchaseType = PurchaseType.CARD,
                        amount = BigDecimal(1500),
                        orderId = orderId,
                        paymentKey = paymentKey,
                        currency = Currency.KRW
                    )
                )
            }
            exception.purchaseExceptionCode shouldBe PurchaseExceptionCode.NOT_VALID_ORDER_ID
        }
    }
})
