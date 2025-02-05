package member.domain.implementation

interface PasswordEncoder {
    fun encode(plainPassword: String): String
    fun matches(plainPassword: String, encodedPassword: String): Boolean
}
