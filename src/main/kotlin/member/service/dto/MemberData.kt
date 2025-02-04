package member.service.dto

import member.domain.entity.Member
import java.util.*

data class MemberData(
    val id: UUID,
    val email: String,
    val password: String
) {
    companion object {
        fun from(member: Member): MemberData {
            return MemberData(
                id = member.getId(),
                email = member.getEmail(),
                password = member.getPassword()
            )
        }
    }
}
