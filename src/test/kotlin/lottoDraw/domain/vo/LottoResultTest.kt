package lottoDraw.domain.vo

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class LottoResultTest{
    @Test
    fun `일치 개수 6개와 보너스 여부는 동시 존재할 수 없다`(){
        assertThrows<IllegalArgumentException> {
            LottoResult(10,6,true)
        }
    }
}
