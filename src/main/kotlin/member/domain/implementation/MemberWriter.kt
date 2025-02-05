package member.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import member.domain.entity.Member
import member.domain.repository.MemberRepository
import member.domain.vo.MemberIdentifier
import util.validateEmail

@Implementation
class MemberWriter(
    private val memberReader: MemberReader,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transaction
    @Write
    fun saveMember(memberIdentifier: MemberIdentifier): Member =

        validateEmail(memberIdentifier.email).run {
            require(!memberReader.existByEmail(memberIdentifier.email)) { "Already Exist Email" }
                .run {
                    memberRepository.save(
                        Member(
                            email = memberIdentifier.email,
                            password = passwordEncoder.encode(memberIdentifier.password)
                        )
                    )
                }
        }
}
