package member.domain.implementation

import common.business.Implementation
import org.mindrot.jbcrypt.BCrypt

@Implementation
class BCryptPasswordEncoder : PasswordEncoder {
    override fun encode(plainPassword: String): String {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
    }

    override fun matches(plainPassword: String, encodedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, encodedPassword)
    }
}
