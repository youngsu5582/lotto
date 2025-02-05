package lotto.domain

import lotto.domain.entity.Lotto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class LottoTest {
    @Test
    fun `숫자들은 자동으로 정렬된다`() {
        val lotto = Lotto(numbers = listOf(32, 44, 1, 4, 2, 3))
        assertThat(lotto).isEqualTo(Lotto(listOf(1, 2, 3, 4, 32, 44)))
    }

    @Test
    fun `숫자는 1부터 45까지 가능하다`() {
        assertThatThrownBy {
            Lotto(
                numbers = listOf(1, 2, 3, 4, 5, 46),
            )
        }
    }

    @Test
    fun `중복된 숫자는 가질수 없다`() {
        assertThatThrownBy {
            Lotto(numbers = listOf(1, 2, 3, 4, 44, 44))
        }
    }

    @Test
    fun `숫자는 6개여야 한다`() {
        assertThatThrownBy {
            Lotto(numbers = listOf(1, 2, 3, 4, 44, 32, 45))
        }
    }

    @Test
    fun `숫자들이 같으면 같은걸로 판단한다`() {
        val lotto1 = Lotto(1, numbers = listOf(1, 2, 3, 4, 32, 44))
        val lotto2 = Lotto(2, numbers = listOf(1, 2, 3, 4, 32, 44))
        assertThat(lotto1).isEqualTo(lotto2).isEqualTo(Lotto(listOf(1, 2, 3, 4, 32, 44)))
    }
}
