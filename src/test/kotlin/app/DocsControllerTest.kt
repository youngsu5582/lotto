package app

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.DocsFieldType
import org.junit.jupiter.api.Test

@AcceptanceTest
class SampleTestControllerTest {

    @Test
    fun `샘플 API 테스트 - Body, Param, Header 동작 확인`() {
        DocsApiBuilder("docs-test")
            .setRequest("/api/test", HttpMethod.GET) {
                body {
                    "stringField" type DocsFieldType.STRING means "문자열 필드" value "testValue"
                    "numberField" type DocsFieldType.NUMBER means "숫자 필드" value 1234
                    "booleanField" type DocsFieldType.BOOLEAN means "참/거짓 필드" value true
                    "arrayField" type DocsFieldType.ARRAY(DocsFieldType.NUMBER) means "숫자 배열 필드" value listOf(
                        1,
                        2,
                        3
                    )
                    "objectField" type DocsFieldType.OBJECT means "응답 데이터" withChildren {
                        "key" type DocsFieldType.STRING means "객체 내부의 키" value "value"
                    }
                }
                headers {
                    "Authorization" type DocsFieldType.STRING means "Authorization 토큰" value "Bearer test-token"
                    "Custom-Header" type DocsFieldType.STRING means "커스텀 헤더" value "CustomValue"
                }
                params {
                    "queryKey1" type DocsFieldType.STRING means "쿼리 파라미터 1" value "queryValue1"
                    "queryKey2" type DocsFieldType.STRING means "쿼리 파라미터 2" value "queryValue2"
                }
            }
            .setResponse {
                body {
                    "headers.authorization" type DocsFieldType.STRING means "Authorization 헤더 값"
                    "headers.custom-header" type DocsFieldType.STRING means "Custom 헤더 값"
                    "headers.accept" type DocsFieldType.STRING means "Accept 헤더 값"
                    "headers.content-type" type DocsFieldType.STRING means "Content-Type 헤더 값"
                    "headers.content-length" type DocsFieldType.STRING means "Content-Length 헤더 값"
                    "headers.host" type DocsFieldType.STRING means "Host 헤더 값"
                    "headers.connection" type DocsFieldType.STRING means "Connection 헤더 값"
                    "headers.user-agent" type DocsFieldType.STRING means "User-Agent 헤더 값"
                    "headers.accept-encoding" type DocsFieldType.STRING means "Accept-Encoding 헤더 값"
                    "params.queryKey1" type DocsFieldType.STRING means "쿼리 파라미터 1 값"
                    "params.queryKey2" type DocsFieldType.STRING means "쿼리 파라미터 2 값"
                    "body.stringField" type DocsFieldType.STRING means "문자열 필드 값"
                    "body.numberField" type DocsFieldType.NUMBER means "숫자 필드 값"
                    "body.booleanField" type DocsFieldType.BOOLEAN means "참/거짓 필드 값"
                    "body.arrayField" type DocsFieldType.ARRAY(DocsFieldType.NUMBER) means "숫자 배열 필드 값"
                    "body.objectField.key" type DocsFieldType.STRING means "객체 내부 키 값"
                }
            }
            .execute(true)
            .statusCode(200)
    }
}
