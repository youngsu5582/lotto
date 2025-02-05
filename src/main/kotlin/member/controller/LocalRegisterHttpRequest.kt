package member.controller

import member.domain.vo.MemberIdentifier

data class LocalRegisterHttpRequest(
    val email: String,
    val password: String
) {
    fun toIdentifier(): MemberIdentifier {
        return MemberIdentifier(
            email = email,
            password = password
        )
    }
}


