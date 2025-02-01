package lotto.controller

import config.AcceptanceTest
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import sendRequest

@AcceptanceTest(["/acceptance/lottoOrder.json"])
class LottoOrderTest {
    @Test
    fun `결제승인을 통해 성공적으로 결제를 진행한다`() {
        val request = createRequest(
            numbers = listOf(listOf(1, 10, 11, 14, 15, 17))
        )

        sendRequest(
            request,
            "order-success",
            commonRequestFields(),
            successResponseFields(),
            200,
            "/api/order"
        )
    }

    private fun createRequest(
        numbers: List<List<Int>>,
    ): Map<String, Any> {
        return mapOf(
            "numbers" to numbers
        )
    }
    private fun commonRequestFields() = requestFields(
        fieldWithPath("numbers").type(JsonFieldType.ARRAY).description("구매할 로또 번호 목록"),
        fieldWithPath("numbers[].[]").type(JsonFieldType.ARRAY).description("로또 번호")
    )

    private fun successResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        fieldWithPath("data.lottoPublishId").type(JsonFieldType.NUMBER).description("발행된 로또 ID"),
        fieldWithPath("data.orderId").type(JsonFieldType.STRING).description("저장된 주문번호 ID"),
    )
}
