package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["common", "lotto", "toss", "purchase"])
@EntityScan(basePackages = ["lotto.domain", "purchase.domain"])
@EnableJpaRepositories(basePackages = ["lotto.domain", "purchase.domain"])
class LottoApplication

fun main(args: Array<String>) {
    runApplication<LottoApplication>(*args)
}
