package fixture

import lottoDraw.domain.entity.LottoDrawEntity
import java.time.LocalDate

object LottoDrawFixture {
    fun 회차_숫자들(round: Int, numbers: List<Byte>): LottoDrawEntity {
        return LottoDrawEntity(
            round = round,
            first = numbers[0],
            second = numbers[1],
            third = numbers[2],
            fourth = numbers[3],
            fifth = numbers[4],
            sixth = numbers[5],
            bonus = numbers[6],
            date = LocalDate.now()
        )
    }
}
