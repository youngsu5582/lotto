package lotto.domain.vo

import io.kotest.core.spec.style.FunSpec
import lotto.domain.entity.IssueStatus
import lotto.domain.entity.IssuedLotto
import lotto.domain.entity.Lotto
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class LottoPaperTest : FunSpec({

    test("로또가 5개 이상이면 예외를 발생한다.") {
        val lotto = List(6) {
            IssuedLotto(
                issueStatus = IssueStatus.MANUAL,
                lotto = Lotto(id = 1, numbers = listOf(1, 2, 3, 11, 14, 17))
            )
        }
        assertThrows<IllegalArgumentException> {
            LottoPaper(
                lotto,
                amount = BigDecimal(2000)
            )
        }
    }
})
