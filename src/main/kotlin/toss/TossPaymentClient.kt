package toss

import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestClient
import toss.dto.TossPaymentCancelRequest
import toss.dto.TossPaymentConfirmRequest
import toss.dto.TossPaymentResponse
import java.util.*

class TossPaymentClient(
    private val restClient: RestClient,
) {

    fun payment(tossPaymentConfirmRequest: TossPaymentConfirmRequest): TossPaymentResponse {

        val value: String = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6" + ":"
        val key = Base64
            .getEncoder()
            .encodeToString(value.toByteArray())
        println(key)
//        dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==
//        dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==
        val result = restClient.post()
            .uri("/v1/payments/confirm")
            .header(HttpHeaders.AUTHORIZATION, "Basic $key")
            .body(tossPaymentConfirmRequest)
            .retrieve()
            .body(TossPaymentResponse::class.java)!!

        val delete = restClient.post()
            .uri("/v1/payments/${tossPaymentConfirmRequest.paymentKey}/cancel")
            .header(HttpHeaders.AUTHORIZATION, "Basic $key")
            .body(TossPaymentCancelRequest("구매자 변심"))
            .retrieve()
            .body(TossPaymentResponse::class.java)!!

        println(delete)

        return restClient.post()
            .uri("/v1/payments/confirm")
            .header(HttpHeaders.AUTHORIZATION, "Basic $key")
            .body(tossPaymentConfirmRequest)
            .retrieve()
            .body(TossPaymentResponse::class.java)!!
        //Error while extracting response for type [toss.dto.TossPaymentResponse] and content type [application/json]
        //401 Unauthorized: "{"code":"UNAUTHORIZED_KEY","message":"인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다.","data":null}"
        //400 Bad Request: "{"code":"ALREADY_PROCESSED_PAYMENT","message":"이미 처리된 결제 입니다."}"
        //404 Not Found: "{"code":"NOT_FOUND_PAYMENT_SESSION","message":"결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다."}"
        //500 Internal Server Error: "{"code":"FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING","message":"결제가능 시간을 초과하였습니다."}"

        //TossPaymentResponse(paymentKey=tgen_20241216235231TWGL3, status=DONE, orderId=MC45ODgyNjYxNTIyODM4, totalAmount=50000, method=SIMPLE_PAY)

        //TossPaymentResponse(paymentKey=tgen_202412170012070fF24, status=CANCELED, orderId=MC42NzA0ODc4NTczNzc1, totalAmount=50000, method=SIMPLE_PAY)
        // 결제 후 다시 처리시
        // 400 Bad Request: "{"code":"ALREADY_PROCESSED_PAYMENT","message":"이미 처리된 결제 입니다."}"
    }
}
