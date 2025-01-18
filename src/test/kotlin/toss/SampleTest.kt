package toss

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import toss.config.TossClientProperties
import kotlin.test.Test

@SpringBootTest
@EnableConfigurationProperties(TossClientProperties::class)
class TossClientPropertiesTest {
    @Autowired
    private lateinit var tossClientProperties: TossClientProperties

    @Test
    fun `properties should be loaded correctly`() {
        assert(tossClientProperties.apiKey == "test_key")
        assert(tossClientProperties.paymentUrl == "/v1/payments/confirm")
    }
}
