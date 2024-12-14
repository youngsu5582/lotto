package lotto.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class LottoPublish(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    private val lottoRoundInfo: LottoRoundInfo,

    private val issuedAt: LocalDateTime,

    @OneToMany(fetch = FetchType.LAZY)
    private val lottoes: List<Lotto>,
)
