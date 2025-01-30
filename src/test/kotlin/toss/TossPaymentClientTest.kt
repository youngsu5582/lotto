import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.matchers.shouldBe
import lotto.domain.vo.Currency
import lotto.domain.vo.PurchaseType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestClient
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.vo.CancelRequest
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseRequest
import toss.TossPaymentClient
import toss.TossPaymentErrorCode
import toss.TossResponseErrorHandler
import toss.config.TossClientProperties
import toss.dto.CancelResponse
import toss.dto.TossPaymentCancelResponse
import toss.dto.TossPaymentConfirmResponse
import toss.dto.TossPaymentErrorResponse
import java.math.BigDecimal
import java.time.LocalDateTime


class TossPaymentClientTest {
    private val baseUrl = "https://api.tosspayments.com"
    private val testBuilder = RestClient.builder().baseUrl(baseUrl)
    private val server = MockRestServiceServer.bindTo(testBuilder).build()
    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

    private val tossPaymentClient: TossPaymentClient = TossPaymentClient(
        testBuilder.build(),
        TossClientProperties(apiKey = "", paymentUrl = "/v1/payments/confirm", "/v1/payments/{paymentKey}/cancel"),
        TossResponseErrorHandler()
    )

    @Nested
    @DisplayName("결제 승인 API")
    inner class PaymentConfirmTest {
        private val request = PurchaseRequest(
            paymentKey = "paymentKey",
            amount = BigDecimal(1000),
            orderId = "orderId",
            currency = Currency.KRW.name,
            purchaseType = PurchaseType.CARD.name
        )
        private val response = TossPaymentConfirmResponse(
            paymentKey = "paymentKey",
            totalAmount = 1000,
            orderId = "orderId",
            method = PaymentMethod.CARD,
            status = "DONE"
        )

        @Test
        fun `결제에 성공한다`() {
            serverSuccess("v1/payments/confirm", response)
            val result = tossPaymentClient.process(request)

            result.paymentKey shouldBe "paymentKey"
            result.orderId shouldBe "orderId"
            result.isSuccess() shouldBe true
        }

        @Test
        fun `결제에 실패한다`() {
            serverFail(
                "v1/payments/confirm", createErrorResponse(
                    TossPaymentErrorCode.ALREADY_PROCESSED_PAYMENT
                )
            )
            assertThrows<PurchaseException> {
                tossPaymentClient.process(request)

            }.purchaseExceptionCode shouldBe PurchaseExceptionCode.FAILED
        }

        @Test
        fun `다시 처리해야 하는 오류일시 재시도 해서 성공한다`() {
            serverFail(
                "v1/payments/confirm",
                createErrorResponse(TossPaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING)
            )
            serverFail(
                "v1/payments/confirm",
                createErrorResponse(TossPaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING)
            )
            serverSuccess("v1/payments/confirm", response)

            val result = tossPaymentClient.process(request)

            result.paymentKey shouldBe "paymentKey"
            result.orderId shouldBe "orderId"
            result.isSuccess() shouldBe true
        }

        @Test
        fun `재시도 횟수가 3번 넘어가면 예외를 발생한다`() {
            repeat(3) {
                serverFail(
                    "v1/payments/confirm",
                    createErrorResponse(TossPaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING)
                )
            }
            assertThrows<PurchaseException> {
                tossPaymentClient.process(request)
            }.purchaseExceptionCode shouldBe PurchaseExceptionCode.RETRY_FAILED
        }
    }

    @Nested
    @DisplayName("결제 취소 API")
    inner class PaymentCancelTest {

        private val request = CancelRequest(
            paymentKey = "paymentKey",
            amount = BigDecimal(1000),
            orderId = "orderId",
            cancelReason = "단순 변심"
        )
        private val response = TossPaymentCancelResponse(
            paymentKey = "paymentKey",
            orderId = "orderId",
            status = "CANCELED",
            cancels = arrayOf(
                CancelResponse(
                    cancelReason = "단순 변심",
                    transactionKey = "transactionKey",
                    cancelAmount = 1000,
                    cancelAt = LocalDateTime.now()
                )
            )
        )

        @Test
        fun `결제취소에 성공한다`() {
            serverSuccess("v1/payments/paymentKey/cancel", response)
            val result = tossPaymentClient.cancel(request)

            result.paymentKey shouldBe "paymentKey"
            result.orderId shouldBe "orderId"
            result.cancels[0].cancelReason shouldBe "단순 변심"
            result.isCanceled() shouldBe true
        }

        @Test
        fun `결제취소에 실패한다`() {
            serverFail(
                "v1/payments/paymentKey/cancel", createErrorResponse(
                    TossPaymentErrorCode.ALREADY_CANCELED_PAYMENT
                )
            )
            assertThrows<PurchaseException> {
                tossPaymentClient.cancel(request)
            }.purchaseExceptionCode shouldBe PurchaseExceptionCode.FAILED
        }
    }

    private fun serverSuccess(url: String, response: Any) {
        server.expect(MockRestRequestMatchers.requestTo("$baseUrl/$url"))
            .andRespond(
                MockRestResponseCreators.withSuccess(
                    objectMapper.writeValueAsString(response),
                    MediaType.APPLICATION_JSON
                )
            )
    }

    private fun serverFail(url: String, response: Any, status: HttpStatus = HttpStatus.BAD_REQUEST) {
        server.expect(MockRestRequestMatchers.requestTo("$baseUrl/$url"))
            .andRespond(
                MockRestResponseCreators.withStatus(status).body(
                    objectMapper.writeValueAsString(response)
                )
            )
    }

    private fun createErrorResponse(tossPaymentErrorCode: TossPaymentErrorCode): TossPaymentErrorResponse {
        return TossPaymentErrorResponse(
            code = tossPaymentErrorCode.code,
            message = tossPaymentErrorCode.message
        )
    }
}
