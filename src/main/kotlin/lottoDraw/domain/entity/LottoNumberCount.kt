package lottoDraw.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "lotto_number_count")
class LottoNumberCount(
    @Id
    @Column(name = "number", nullable = false)
    var number: Byte = 0,

    @Column(name = "count", nullable = false)
    var count: Int = 0,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
) {}

