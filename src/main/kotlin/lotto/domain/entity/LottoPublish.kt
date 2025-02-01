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
    @JoinTable(
        name = "lotto_publish_lotto",
        joinColumns = [JoinColumn(name = "lotto_publish_id")],
        inverseJoinColumns = [JoinColumn(name = "lotto_id")],

    )
    private val lottoes: List<Lotto>,
    @Convert(converter = IssueStatusListConverter::class)
    private val issuedLottoesStatus: List<IssueStatus>,
    private var canceled: Boolean = false,
) {
    fun getId() = id?: throw IllegalArgumentException("Not Exist Id")
    fun getLottoRoundInfo() = lottoRoundInfo
    fun getIssuedAt() = issuedAt
    fun getLottoes() = lottoes
    fun getIssuedLottoesStatus() = issuedLottoesStatus
    fun getCanceled() = canceled
    fun cancel() {
        this.canceled = true
    }
}
