package lotto.domain.vo

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LottoNumberTest {
    @ParameterizedTest
    @ValueSource(bytes = [0, 46])
    fun `1부터 45이하 숫자만 가능하다`(number: Byte) {
        assertThrows<IllegalArgumentException> {
            LottoNumber(number = number)
        }
    }
}
