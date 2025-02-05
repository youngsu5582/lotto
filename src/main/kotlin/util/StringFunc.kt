package util

const val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"

fun validateEmail(s: String) {
    require(s.matches(emailRegex.toRegex())) { "올바르지 않은 이메일 형식입니다." }
}
