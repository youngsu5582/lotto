package lotto.domain.repository

import lotto.domain.entity.LottoBill
import org.springframework.data.jpa.repository.JpaRepository

interface LottoBillRepository : JpaRepository<LottoBill, Long>
