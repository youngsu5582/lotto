package lotto.domain.repository

import lotto.domain.LottoPublish
import org.springframework.data.repository.CrudRepository

interface LottoPublishRepository : CrudRepository<LottoPublish, Long> {
}
