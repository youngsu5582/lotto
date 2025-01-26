package toss

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.vo.PurchaseRequest
import toss.config.TossClientProperties
import java.math.BigDecimal

@EnableConfigurationProperties(TossClientProperties::class)
class TossPaymentClientTimeoutTest {
    private val connectTimeoutUrl = "https://10.255.255.1"
    private val readTimeoutUrl = "https://httpbin.org/delay/9"
    private val properties = TossClientProperties()

    @Test
    fun `결제중 CONNECT TIMEOUT 이 발생하면, 예외를 던진다`() {
        val client = getTossTestClient(url = connectTimeoutUrl)
        assertThrows<PurchaseException> {
            client.process(
                request = PurchaseRequest(
                    paymentKey = "paymentKey",
                    amount = BigDecimal(1000),
                    orderId = "orderId",
                    currency = "KRW",
                    purchaseType = "CARD"
                )
            )
        }.let { Assertions.assertThat(it.purchaseExceptionCode).isEqualTo(PurchaseExceptionCode.CONNECT_TIMEOUT) }
    }

    @Test
    fun `결제중 READ TIMEOUT 이 발생하면, 예외를 던진다`() {
        val client = getTossTestClient(url = readTimeoutUrl, readTimeout = 1)
        assertThrows<PurchaseException> {
            client.process(
                request = PurchaseRequest(
                    paymentKey = "paymentKey",
                    amount = BigDecimal(1000),
                    orderId = "orderId",
                    currency = "KRW",
                    purchaseType = "CARD"
                )
            )
        }.let { Assertions.assertThat(it.purchaseExceptionCode).isEqualTo(PurchaseExceptionCode.READ_TIMEOUT) }
    }

    private fun getTossTestClient(
        testCode: TossPaymentErrorCode = TossPaymentErrorCode.UNKNOWN_PAYMENT_ERROR,
        url: String = "https://api.tosspayments.com",
        readTimeout: Int = 1000
    ): TossPaymentClient {
        val restClient = RestClient.builder().baseUrl(url)
            .requestFactory(getClientHttpRequestFactory(readTimeout))
            .defaultHeader("TossPayments-Test-Code", testCode.code).build()
        return TossPaymentClient(restClient, properties, TossResponseErrorHandler())
    }

    private fun getClientHttpRequestFactory(readTimeout: Int = 1000): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(readTimeout)
        factory.setConnectTimeout(1000)
        return factory
    }
}
