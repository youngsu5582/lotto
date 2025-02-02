package lotto.domain.entity

import jakarta.persistence.*
import lotto.domain.converter.IssueStatusConverter

@Entity
@Table(name = "published_lotto")
class PublishedLotto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotto_publish_id", nullable = false)
    private val lottoPublish: LottoPublish,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotto_id", nullable = false)
    private val lotto: Lotto,

    @Convert(converter = IssueStatusConverter::class)
    private val status: IssueStatus
) {

}
