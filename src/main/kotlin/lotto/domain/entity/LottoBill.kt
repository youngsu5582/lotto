package lotto.domain.entity

import jakarta.persistence.*
import purchase.domain.entity.Purchase

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
    @OneToOne
    private val purchase: Purchase,
    @OneToOne
    private val lottoPublish: LottoPublish,
){
    fun getId() = id
    fun getPurchase() = purchase
    fun getLottoPublish() = lottoPublish
}
