package config

import java.time.*

class MockingClock : Clock() {
    private var instant: Instant? = null

    fun setInstant(instant: Instant) {
        this.instant = instant
    }

    fun setInstant(localDateTime: LocalDateTime) {
        this.instant = localDateTime?.toInstant(ZoneOffset.UTC)
    }

    override fun instant(): Instant {
        return instant!!
    }

    override fun getZone(): ZoneId {
        return ZoneOffset.UTC
    }

    override fun withZone(zoneId: ZoneId): Clock {
        throw UnsupportedOperationException()
    }
}
