package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["lotto", "purchase", "toss", "common"])
@EntityScan(basePackages = ["lotto", "purchase"])
@EnableJpaRepositories(basePackages = ["lotto", "purchase"])
class LottoApplication

fun main(args: Array<String>) {
    runApplication<LottoApplication>(*args)
}
