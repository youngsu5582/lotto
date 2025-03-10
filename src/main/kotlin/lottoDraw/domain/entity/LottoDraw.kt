package lottoDraw.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class LottoDraw(
    @Id
    @Column(name = "round")
    val round: Int = 0,

    @Column(name = "first_num")
    val first: Byte = 0,

    @Column(name = "second_num")
    val second: Byte = 0,

    @Column(name = "third_num")
    val third: Byte = 0,

    @Column(name = "fourth_num")
    val fourth: Byte = 0,

    @Column(name = "fifth_num")
    val fifth: Byte = 0,

    @Column(name = "sixth_num")
    val sixth: Byte = 0,

    @Column(name = "bonus")
    val bonus: Byte = 0,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
){
}


