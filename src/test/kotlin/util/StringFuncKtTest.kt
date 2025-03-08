package util

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class StringFuncKtTest{
    @Test
    fun `앞 뒤 4개를 제외하고 마스킹한다`(){
        val secretKey = "ABCDAAEFGAAHIJK"
        secretKey.masking() shouldBe  "ABCD****HIJK"
    }
    @Test
    fun `4글자 이하이면 ****로 마스킹한다`(){
        val secretKey = "AAAA"
        secretKey.masking() shouldBe "****"
    }
}
