package config

import app.LottoApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [LottoApplication::class])
@Sql(value = ["/clear.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
annotation class RepositoryTest
