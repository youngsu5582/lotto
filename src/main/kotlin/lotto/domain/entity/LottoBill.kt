package lotto.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    indexes = [
        Index(name = "idx_numbers", columnList = "numbers"),
    ],
)
class LottoBill(
    @Id
    @GeneratedValue
    private val id: Long? = null,
    private val purchaseId: UUID?,
    @OneToOne
    private val lottoPublish: LottoPublish,
)
