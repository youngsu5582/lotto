package toss

import common.business.Implementation
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestClient
import purchase.domain.PaymentClient
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest
import toss.config.TossClientProperties
import toss.dto.TossPaymentConfirmRequest
import toss.dto.TossPaymentResponse
import java.math.BigDecimal
import java.util.*


@Implementation
class TossPaymentClient(
    private val restClient: RestClient,
    private val tossClientProperties: TossClientProperties,
) : PaymentClient {
    override fun process(request: PurchaseRequest): PurchaseData {
        val tossPaymentConfirmRequest =
            TossPaymentConfirmRequest(
                paymentKey = request.paymentKey,
                orderId = request.orderId,
                amount = request.amount.toLong(),
            )
        val value: String = tossClientProperties.apiKey + ":"
        val key =
            Base64
                .getEncoder()
                .encodeToString(value.toByteArray())

        val response =
            restClient.post()
                .uri(tossClientProperties.paymentUrl)
                .header(HttpHeaders.AUTHORIZATION, "Basic $key")
                .body(tossPaymentConfirmRequest)
                .retrieve()
                .body(TossPaymentResponse::class.java)!!
        return PurchaseData(
            paymentKey = response.paymentKey,
            status = response.status,
            method = response.method,
            purchaseProvider = PurchaseProvider.TOSS,
            orderId = response.orderId,
            totalAmount = BigDecimal(response.totalAmount),
        )

//        val delete = restClient.post()
//            .uri("/v1/payments/${tossPaymentConfirmRequest.paymentKey}/cancel")
//            .header(HttpHeaders.AUTHORIZATION, "Basic $key")
//            .body(TossPaymentCancelRequest("구매자 변심"))
//            .retrieve()
//            .body(TossPaymentResponse::class.java)!!
//
//        println(delete)
//
//        return restClient.post()
//            .uri("/v1/payments/confirm")
//            .header(HttpHeaders.AUTHORIZATION, "Basic $key")
//            .body(tossPaymentConfirmRequest)
//            .retrieve()
//            .body(TossPaymentResponse::class.java)!!
        // Error while extracting response for type [toss.dto.TossPaymentResponse] and content type [application/json]
        // 401 Unauthorized: "{"code":"UNAUTHORIZED_KEY","message":"인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다.","data":null}"
        // 400 Bad Request: "{"code":"ALREADY_PROCESSED_PAYMENT","message":"이미 처리된 결제 입니다."}"
        // 404 Not Found: "{"code":"NOT_FOUND_PAYMENT_SESSION","message":"결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다."}"
        // 500 Internal Server Error: "{"code":"FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING","message":"결제가능 시간을 초과하였습니다."}"

        // TossPaymentResponse(paymentKey=tgen_20241216235231TWGL3, status=DONE, orderId=MC45ODgyNjYxNTIyODM4, totalAmount=50000, method=SIMPLE_PAY)

        // TossPaymentResponse(paymentKey=tgen_202412170012070fF24, status=CANCELED, orderId=MC42NzA0ODc4NTczNzc1, totalAmount=50000, method=SIMPLE_PAY)
        // 결제 후 다시 처리시
        // 400 Bad Request: "{"code":"ALREADY_PROCESSED_PAYMENT","message":"이미 처리된 결제 입니다."}"
    }
}
