package auth.domain.vo

data class AccessToken(
    val value: String
) {
    fun value(): String = value
}
