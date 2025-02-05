package member.domain.implementation

import common.business.Implementation
import member.domain.entity.Member
import member.domain.repository.MemberRepository
import member.domain.vo.MemberIdentifier

@Implementation
class MemberReader(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun findMember(memberIdentifier: MemberIdentifier): Member =
        (memberRepository.findByEmail(memberIdentifier.email)
            ?: throw NoSuchElementException("이메일(${memberIdentifier.email})에 해당하는 회원을 찾을 수 없습니다"))
            .also {
                if (passwordEncoder.matches(it.getPassword(), memberIdentifier.password).not()) {
                    throw IllegalArgumentException("비밀번호가 일치하지 않습니다")
                }
            }
    fun existByEmail(email: String) = memberRepository.findByEmail(email) != null
}
