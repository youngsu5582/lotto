package lotto.domain.implementation

import app.TestConfig
import config.ImplementationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.context.annotation.Import
import purchase.domain.PurchaseException
import purchase.domain.entity.Purchase
import purchase.domain.entity.PurchaseInfo
import purchase.domain.implementation.PurchaseProcessor
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseProvider
import java.math.BigDecimal

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseProcessorTest(
    private val purchaseProcessor: PurchaseProcessor,
    private val purchaseRepository: PurchaseRepository
) : FunSpec({
    extensions(listOf(SpringExtension))

    context("결제 취소") {
        test("결제 상태를 검증한다.") {
            assertDoesNotThrow {
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

                purchaseProcessor.cancel(
                    purchase.getId().toString(),
                )
            }
        }
        test("결제가 성공 상태가 아니면 예외를 발생한다.") {
            shouldThrow<PurchaseException> {
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


                purchaseProcessor.cancel(
                    failPurchase.getId().toString(),
                )
            }
        }
    }
})
