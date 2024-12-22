package lotto.domain.lotto.repository

import lotto.domain.lotto.LottoPublish
import org.springframework.data.repository.CrudRepository

interface LottoPublishRepository : CrudRepository<LottoPublish, Long> {
}
