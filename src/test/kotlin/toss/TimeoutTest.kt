package toss

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient

class TimeoutTest {
    private val restClient = createClient()
    private val connectTimeoutUrl = "https://10.255.255.1"
    private val readTimeoutUrl = "https://httpbin.org/delay/9"


    @Test
    @Disabled
    fun `메시지를 통해 Connect,Read 를 구분한다`() {
        assertThrows<ResourceAccessException> {
            restClient.get()
                .uri(connectTimeoutUrl)
                .retrieve()
                .body(String::class.java)
        }.also {
            assertThat(it.message).contains("Connect timed out")
        }

        assertThrows<ResourceAccessException> {
            restClient.get()
                .uri(readTimeoutUrl)
                .retrieve().body(String::class.java)
        }.also {
            assertThat(it.message).contains("Read timed out")
        }
    }

    private fun createClient(): RestClient {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(1000)
        factory.setConnectTimeout(8000)
        return RestClient
            .builder()
            .requestFactory(factory)
            .build()
    }
}
