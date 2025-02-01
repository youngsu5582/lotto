import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

fun sendRequest(
    request: Map<String, Any>,
    documentName: String,
    requestFields: RequestFieldsSnippet,
    responseFields: ResponseFieldsSnippet,
    expectedStatus: Int,
    url: String,
    map: Map<String, Any> = HashMap()
) {
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .headers(map)
        .body(request)
        .filter(
            RestAssuredRestDocumentation.document(
                documentName,
                requestFields,
                responseFields
            )
        )
        .post(url).then().log().all().statusCode(expectedStatus).extract()
}
