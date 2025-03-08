package app

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
@Profile("prod")
class ConnectionCheck(
    private val dataSource: DataSource,
    private val redisTemplate: StringRedisTemplate
) {

    private val log: Logger = LoggerFactory.getLogger(ConnectionCheck::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun checkConnections() {
        try {
            dataSource.connection.use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery("SELECT 1")
                    if (rs.next()) {
                        log.info("DB connection check success! (Result: ${rs.getInt(1)})")
                    } else {
                        log.warn("DB connection check returned no rows!")
                    }
                }
            }
        } catch (e: Exception) {
            log.error("DB connection check failed: ${e.message}", e)
        }

        try {
            redisTemplate.opsForValue().set("testKey", "testValue")
            val value = redisTemplate.opsForValue().get("testKey")
            if ("testValue" == value) {
                log.info("Redis connection check success! (testKey=$value)")
            } else {
                log.warn("Redis connection check failed! (expected testValue, got=$value)")
            }
        } catch (e: Exception) {
            log.error("Redis connection check failed: ${e.message}", e)
        }
    }
}
