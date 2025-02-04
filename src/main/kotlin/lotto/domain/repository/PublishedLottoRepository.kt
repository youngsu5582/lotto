package lotto.domain.repository

import lotto.domain.entity.PublishedLotto
import org.springframework.data.repository.CrudRepository

interface PublishedLottoRepository : CrudRepository<PublishedLotto, Long> {
}
