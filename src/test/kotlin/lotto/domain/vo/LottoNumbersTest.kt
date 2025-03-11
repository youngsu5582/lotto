package lotto.domain.vo

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LottoNumbersTest {
    @ParameterizedTest
    @ValueSource(ints = [5, 7])
    fun `6개의 숫자로 구성되어야 한다`(size: Int) {
        val numbers = (1..size).map { it.toByte() }.toList()

        assertThrows<IllegalArgumentException> {
            LottoNumbers.from(numbers)
        }
    }
}
