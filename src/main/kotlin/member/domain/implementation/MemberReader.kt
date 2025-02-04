package member.domain.implementation

import common.business.Implementation
import member.domain.entity.Member
import member.domain.repository.MemberRepository
import member.domain.vo.MemberIdentifier

@Implementation
class MemberReader(
    private val memberRepository: MemberRepository
) {
    fun findMember(memberIdentifier: MemberIdentifier): Member =
        (memberRepository.findByEmail(memberIdentifier.email)
            ?: throw NoSuchElementException("Member not found with email: ${memberIdentifier.email}"))
            .also {
                if (it.isNotEqualPassword(memberIdentifier.password)) {
                    throw IllegalArgumentException("Not Equal Password")
                }
            }

    fun existByEmail(email: String) = memberRepository.findByEmail(email) != null
}
