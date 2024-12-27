package config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@DataJpaTest
@EntityScan(basePackages = ["lotto.domain", "purchase.domain"])
@EnableJpaRepositories(basePackages = ["lotto.domain.repository", "purchase.domain.repository"])


annotation class RepositoryTest
