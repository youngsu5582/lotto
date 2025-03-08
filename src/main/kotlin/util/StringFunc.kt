package util

const val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"

fun validateEmail(s: String) {
    require(s.matches(emailRegex.toRegex())) { "올바르지 않은 이메일 형식입니다." }
}

fun String.masking(): String =
    with(this) { if (length > 8) substring(0, 4) + "****" + substring(length - 4) else "****" }

