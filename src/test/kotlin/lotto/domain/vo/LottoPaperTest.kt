package lotto.domain.vo

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import lotto.domain.entity.IssueStatus
import lotto.domain.entity.IssuedLotto
import lotto.domain.entity.Lotto
import java.math.BigDecimal

class LottoPaperTest : FunSpec({

    context("로또 페이퍼 식별자 생성") {
        test("ID와 발급 상태들로 생성한다.") {
            val lottoPaper = LottoPaper(
                listOf(
                    IssuedLotto(
                        issueStatus = IssueStatus.MANUAL,
                        lotto = Lotto(id = 1, numbers = listOf(1, 2, 3, 11, 14, 17))
                    ),
                    IssuedLotto(
                        issueStatus = IssueStatus.AUTO,
                        lotto = Lotto(id = 2, numbers = listOf(1, 2, 3, 11, 14, 31))
                    )
                ),
                amount = BigDecimal(2000)
            )
            lottoPaper.getLottoes().map { it.getId() } shouldBe listOf(1, 2)
            lottoPaper.getIssuedStatues() shouldBe listOf(IssueStatus.MANUAL, IssueStatus.AUTO)
        }
    }
})
