package member.domain.implementation

import common.business.Implementation
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.log

@Implementation
class BCryptPasswordEncoder : PasswordEncoder {
    private val logger: Logger = LoggerFactory.getLogger(BCryptPasswordEncoder::class.java)
    override fun encode(plainPassword: String): String {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
    }

    override fun matches(plainPassword: String, encodedPassword: String): Boolean {
        return try {
            BCrypt.checkpw(plainPassword, encodedPassword)
        } catch (e: IllegalArgumentException) {
            logger.info(e.message)
            false
        }
    }
}
