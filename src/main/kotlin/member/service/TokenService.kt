package member.service

import common.business.BusinessService
import member.domain.implementation.TokenGenerator
import member.domain.vo.Token
import java.util.*

@BusinessService
class TokenService(
    private val tokenGenerator: TokenGenerator
) {
    fun createToken(id: UUID): Token {
        return Token(tokenGenerator.generateAccessToken(id.toString()))
    }
}
