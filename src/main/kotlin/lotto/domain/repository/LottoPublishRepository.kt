package lotto.domain.repository

import lotto.domain.entity.LottoPublish
import org.springframework.data.repository.CrudRepository

interface LottoPublishRepository : CrudRepository<LottoPublish, Long>
