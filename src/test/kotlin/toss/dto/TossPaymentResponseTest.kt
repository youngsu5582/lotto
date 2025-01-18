package toss.dto

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import purchase.domain.vo.PaymentMethod
import kotlin.test.Test

class TossPaymentResponseTest {
    @Test
    fun `역직렬화를 통해 메소드를 ENUM 에 맞게 매핑한다`() {
        val objectMapper = jacksonObjectMapper()

        val json1 =
            """
            {
                "paymentKey": "123456",
                "status": "DONE",
                "orderId": "order-001",
                "totalAmount": 50000,
                "method": "CARD"
            }
            """.trimIndent()

        val json2 =
            """
            {
                "paymentKey": "123457",
                "status": "DONE",
                "orderId": "order-002",
                "totalAmount": 50000,
                "method": "카드"
            }
            """.trimIndent()

        val json3 =
            """
            {
                "paymentKey": "123457",
                "status": "DONE",
                "orderId": "order-002",
                "totalAmount": 50000,
                "method": "Tarjeta"
            }
            """.trimIndent()

        val response1 = objectMapper.readValue(json1, TossPaymentResponse::class.java)
        val response2 = objectMapper.readValue(json2, TossPaymentResponse::class.java)
        val response3 = objectMapper.readValue(json3, TossPaymentResponse::class.java)

        assertThat(response1.method)
            .isEqualTo(response2.method)
            .isEqualTo(response3.method)
            .isEqualTo(PaymentMethod.CARD)
    }
}
