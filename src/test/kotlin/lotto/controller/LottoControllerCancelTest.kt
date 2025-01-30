package lotto.controller

import config.AcceptanceTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

@AcceptanceTest(["/acceptance/lottoCancel.json"])
class LottoControllerCancelTest {
    @Test
    fun `결제취소를 한다`() {
        val request = createRequest(
            billId = 1
        )

        sendRequest(
            request,
            "cancel-success",
            commonRequestFields(),
            successResponseFields(),
            200
        )
    }

    @Test
    fun `결제취소 실패를 한다`() {
        val request = createRequest(
            billId = 2
        )

        sendRequest(
            request,
            "cancel-failure",
            commonRequestFields(),
            errorResponseFields(),
            400
        )
    }

    private fun createRequest(
        billId: Long
    ): Map<String, Any> {
        return mapOf(
            "billId" to billId
        )
    }

    private fun sendRequest(
        request: Map<String, Any>,
        documentName: String,
        requestFields: RequestFieldsSnippet,
        responseFields: ResponseFieldsSnippet,
        expectedStatus: Int
    ) {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .filter(
                RestAssuredRestDocumentation.document(
                    documentName,
                    requestFields,
                    responseFields
                )
            )
            .post("/api/cancel").then().log().all().statusCode(expectedStatus).extract()
    }

    private fun commonRequestFields() = requestFields(
        fieldWithPath("billId").type(JsonFieldType.NUMBER)
            .description("구매했던 영수증 ID"),
    )

    private fun successResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        fieldWithPath("data.purchaseResponse").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("data.purchaseResponse.id").type(JsonFieldType.STRING).description("발행된 로또 번호"),
        fieldWithPath("data.purchaseResponse.amount").type(JsonFieldType.NUMBER).description("발행 일시")
    )

    private fun errorResponseFields() = responseFields(
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
    )
}
