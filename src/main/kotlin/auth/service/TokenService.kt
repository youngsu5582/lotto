package auth.service

import auth.domain.TokenGenerator
import auth.domain.vo.AccessToken
import auth.domain.vo.Token
import common.business.BusinessService
import java.util.*

@BusinessService
class TokenService(
    private val tokenGenerator: TokenGenerator
) {
    fun createToken(id: UUID): Token {
        return Token(tokenGenerator.generateAccessToken(id.toString()))
    }

    fun decodeToken(token: AccessToken): String {
        val claims = tokenGenerator.decodeToken(token)
        return claims.subject
    }
}
