package member.domain.repository

import member.domain.entity.Member
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MemberRepository : CrudRepository<Member, UUID> {
    fun findByEmail(email: String): Member?
}
