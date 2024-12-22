package lotto.domain.entity

import jakarta.persistence.*
import lotto.domain.converter.LottoNumbersConverter

@Entity
@Table(
    indexes = [
        Index(name = "idx_numbers", columnList = "numbers")
    ]
)
class Lotto(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @Convert(converter = LottoNumbersConverter::class)
    private val numbers: List<Int>
) {
    companion object {
        private const val REQUIRED_SIZE = 6
        private const val MIN_NUMBER = 1
        private const val MAX_NUMBER = 45
        private const val MAX_LOTTO_ENTRIES = 10
    }

    init {
        require(numbers.size in 1..MAX_LOTTO_ENTRIES) {
            "로또 번호는 최소 1개에서 최대 $MAX_LOTTO_ENTRIES 개까지 입력할 수 있습니다."
        }
        require(numbers.distinct().size == REQUIRED_SIZE) {
            "로또 번호 내에 중복된 숫자가 존재합니다."
        }
        require(numbers.size == REQUIRED_SIZE) {
            "각 로또 번호는 정확히 $REQUIRED_SIZE 개의 숫자가 필요합니다."
        }
        require(numbers.all { it in MIN_NUMBER..MAX_NUMBER }) {
            "각 로또 번호는 $MIN_NUMBER 부터 $MAX_NUMBER 사이의 숫자여야 합니다."
        }
    }

    constructor(numbers: List<Int>) : this(null, numbers.sorted())

    fun toIntList(): List<Int> {
        return numbers
    }

    fun getId(): Long? = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Lotto) return false

        return numbers == other.numbers
    }

    override fun hashCode(): Int {
        return numbers.hashCode()
    }
}
