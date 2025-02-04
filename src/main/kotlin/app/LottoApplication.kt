package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["lotto", "purchase", "order","toss", "common"])
@EntityScan(basePackages = ["lotto", "purchase", "order"])
@EnableJpaRepositories(basePackages = ["lotto", "purchase", "order"])
class LottoApplication

fun main(args: Array<String>) {
    runApplication<LottoApplication>(*args)
}
