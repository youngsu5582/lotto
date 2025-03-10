package lottoDraw.domain.vo

data class LottoResults(
    val lottoResults : List<LottoResult>
){
    fun toIterable(): Iterable<LottoResult> = lottoResults
}
