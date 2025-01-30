package lotto.domain.implementation

import app.TestConfig
import config.ImplementationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import lotto.domain.vo.Currency
import lotto.domain.vo.PurchaseType
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.context.annotation.Import
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.implementation.OrderDataRequest
import purchase.domain.implementation.PurchaseProcessor
import purchase.domain.implementation.PurchaseValidator
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest
import java.math.BigDecimal

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseProcessorTest(
    private val purchaseProcessor: PurchaseProcessor,
    private val purchaseValidator: PurchaseValidator,
    private val purchaseRepository: PurchaseRepository
) : FunSpec({
    val orderId = "orderId"
    val paymentKey = "paymentKey"
    val amount = BigDecimal(1000)
    extensions(listOf(SpringExtension))

    context("주문에 대한 사전 정보가 있는지 확인한다") {
        beforeTest {
            purchaseValidator.saveTemporary(
                request = OrderDataRequest(
                    orderId = orderId,
                    amount = amount
                )
            )
        }
        test("사전 정보와 일치한지 검증한다.") {
            val result = purchaseProcessor.purchase(
                PurchaseRequest(
                    purchaseType = PurchaseType.CARD.name,
                    amount = BigDecimal(1000),
                    orderId = orderId,
                    paymentKey = paymentKey,
                    currency = Currency.KRW.name
                )
            )
            result.isSuccess() shouldBe true
            result.getId() shouldNotBe null
        }

        test("orderId 에 해당하는 값이 없으면 예외를 발생한다.") {
            val exception = shouldThrow<PurchaseException> {
                purchaseProcessor.purchase(
                    PurchaseRequest(
                        purchaseType = PurchaseType.CARD.name,
                        amount = amount,
                        orderId = "not Exist",
                        paymentKey = paymentKey,
                        currency = Currency.KRW.name
                    )
                )
            }
            exception.purchaseExceptionCode shouldBe PurchaseExceptionCode.NOT_EXIST_ORDER_ID
        }

        test("Id 에 해당하는 금액이 다르면 예외를 발생한다.") {
            val exception = shouldThrow<PurchaseException> {
                purchaseProcessor.purchase(
                    PurchaseRequest(
                        purchaseType = PurchaseType.CARD.name,
                        amount = BigDecimal(1500),
                        orderId = orderId,
                        paymentKey = paymentKey,
                        currency = Currency.KRW.name
                    )
                )
            }
            exception.purchaseExceptionCode shouldBe PurchaseExceptionCode.NOT_VALID_ORDER_ID
        }
    }
    context("결제 취소") {
        val failPurchase = purchaseRepository.save(
            Purchase(
                purchaseProvider = PurchaseProvider.TOSS,
                orderId = "orderId",
                paymentKey = "failPaymentKey",
                status = "FAIL",
                purchaseInfo = PurchaseInfo(
                    totalAmount = BigDecimal(1000),
                    method = PaymentMethod.CARD
                )
            )
        )
        val purchase = purchaseRepository.save(
            Purchase(
                purchaseProvider = PurchaseProvider.TOSS,
                orderId = "orderId",
                paymentKey = "paymentKey",
                status = "SUCCESS",
                purchaseInfo = PurchaseInfo(
                    totalAmount = BigDecimal(1000),
                    method = PaymentMethod.CARD
                )
            )
        )
        test("결제 상태를 검증한다.") {
            assertDoesNotThrow {
                purchaseProcessor.cancel(
                    purchase,
                )
            }
        }
        test("결제가 성공 상태가 아니면 예외를 발생한다.") {
            shouldThrow<PurchaseException> {
                purchaseProcessor.cancel(
                    failPurchase,
                )
            }
        }
    }
})
