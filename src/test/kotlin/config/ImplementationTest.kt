package config

import app.LottoApplication
import app.TestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = [LottoApplication::class],
    properties = ["BATCH_ENABLED=true"]
)
@Import(TestConfig::class)
@Sql(value = ["/clear.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
annotation class ImplementationTest
