package app

import config.AcceptanceTest
import docs.DocsApiBuilder
import docs.HttpMethod
import docs.field.*
import docs.request.body
import docs.request.headers
import docs.request.params
import org.junit.jupiter.api.Test

@AcceptanceTest
class SampleTestControllerTest {

    @Test
    fun `샘플 API 테스트 - Body, Param, Header 동작 확인`() {
        DocsApiBuilder("docs-test")
            .setRequest("/api/test", HttpMethod.GET) {
                body {
                    field {
                        "stringField" type DocsFieldType.STRING means "문자열 필드" value "testValue"
                    }
                    field {
                        "numberField" type DocsFieldType.NUMBER means "숫자 필드" value 1234
                    }
                    field {
                        "booleanField" type DocsFieldType.BOOLEAN means "참/거짓 필드" value true
                    }
                    field {
                        "arrayField" type DocsFieldType.ARRAY(DocsFieldType.NUMBER) means "숫자 배열 필드" value listOf(
                            1,
                            2,
                            3
                        )
                    }
                    field {
                        "objectField" type DocsFieldType.OBJECT means "응답 데이터" withChildren {
                            field { "key" type DocsFieldType.STRING means "객체 내부의 키" value "value" }
                        }
                    }
                }
                headers {
                    field {
                        "Authorization" type DocsFieldType.STRING means "Authorization 토큰" value "Bearer test-token"
                    }
                    field {
                        "Custom-Header" type DocsFieldType.STRING means "커스텀 헤더" value "CustomValue"
                    }
                }
                params {
                    field {
                        "queryKey1" type DocsFieldType.STRING means "쿼리 파라미터 1" value "queryValue1"
                    }
                    field {
                        "queryKey2" type DocsFieldType.STRING means "쿼리 파라미터 2" value "queryValue2"
                    }
                }
            }
            .setResponse {
                body {
                    field { "headers.authorization" type DocsFieldType.STRING means "Authorization 헤더 값" }
                    field { "headers.custom-header" type DocsFieldType.STRING means "Custom 헤더 값" }
                    field { "headers.accept" type DocsFieldType.STRING means "Accept 헤더 값" }
                    field { "headers.content-type" type DocsFieldType.STRING means "Content-Type 헤더 값" }
                    field { "headers.content-length" type DocsFieldType.STRING means "Content-Length 헤더 값" }
                    field { "headers.host" type DocsFieldType.STRING means "Host 헤더 값" }
                    field { "headers.connection" type DocsFieldType.STRING means "Connection 헤더 값" }
                    field { "headers.user-agent" type DocsFieldType.STRING means "User-Agent 헤더 값" }
                    field { "headers.accept-encoding" type DocsFieldType.STRING means "Accept-Encoding 헤더 값" }
                    field { "params.queryKey1" type DocsFieldType.STRING means "쿼리 파라미터 1 값" }
                    field { "params.queryKey2" type DocsFieldType.STRING means "쿼리 파라미터 2 값" }
                    field { "body.stringField" type DocsFieldType.STRING means "문자열 필드 값" }
                    field { "body.numberField" type DocsFieldType.NUMBER means "숫자 필드 값" }
                    field { "body.booleanField" type DocsFieldType.BOOLEAN means "참/거짓 필드 값" }
                    field { "body.arrayField" type DocsFieldType.ARRAY(DocsFieldType.NUMBER) means "숫자 배열 필드 값" }
                    field { "body.objectField.key" type DocsFieldType.STRING means "객체 내부 키 값" }
                }
            }
            .execute(true)
            .statusCode(200)
    }
}
