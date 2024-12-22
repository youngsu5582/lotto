package lotto.domain.entity

import jakarta.persistence.*
import lotto.domain.converter.IssueStatusListConverter
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

    @Convert(converter = IssueStatusListConverter::class)
    private val issuedLottoesStatus: List<IssueStatus>,
)
