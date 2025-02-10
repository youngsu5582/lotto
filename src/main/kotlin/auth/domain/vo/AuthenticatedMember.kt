package auth.domain.vo

data class AuthenticatedMember(
    val id: String,
    val email: String
) : Authenticated {
    override val memberId: String
        get() = id
}
