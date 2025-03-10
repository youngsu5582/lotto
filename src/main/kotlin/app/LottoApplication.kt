package app

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["lotto", "purchase", "order", "toss", "common", "member", "app", "auth","lottoDraw"])
@EntityScan(basePackages = ["lotto", "purchase", "order", "member","lottoDraw"])
@EnableJpaRepositories(basePackages = ["lotto", "purchase", "order", "member","lottoDraw"])
class LottoApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
    runApplication<LottoApplication>(*args)
    log.info { "Server started successfully!" }
}
