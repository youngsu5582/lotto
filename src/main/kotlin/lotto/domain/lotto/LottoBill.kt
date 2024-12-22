package lotto.domain.lotto

import jakarta.persistence.*
import purchase.domain.Purchase

@Entity
@Table(
    indexes = [
        Index(name = "idx_numbers", columnList = "numbers")
    ]
)
class LottoBill(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @OneToOne
    private val purchase: Purchase,

    @OneToOne
    private val lottoPublish: LottoPublish
)
