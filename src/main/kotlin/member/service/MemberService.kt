package member.service

import common.business.BusinessService
import common.business.Transaction
import common.business.Write
import member.domain.implementation.MemberReader
import member.domain.implementation.MemberWriter
import member.domain.vo.MemberIdentifier
import member.service.dto.MemberData

@BusinessService
class MemberService(
    private val memberReader: MemberReader,
    private val memberWriter: MemberWriter
) {
    @Transaction
    @Write
    fun registerMember(memberIdentifier: MemberIdentifier): MemberData =
        MemberData.from(memberWriter.saveMember(memberIdentifier))

    fun readMember(memberIdentifier: MemberIdentifier): MemberData =
        MemberData.from(memberReader.findMember(memberIdentifier))
}
