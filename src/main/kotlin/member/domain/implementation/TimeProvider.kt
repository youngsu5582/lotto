package member.domain.implementation

import common.business.Implementation
import java.util.*

@Implementation
class TimeProvider {
    fun now(): Date {
        return Date()
    }
}
