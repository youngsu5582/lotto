package config

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.LogConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestContextAnnotationUtils
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.util.StreamUtils
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

class AcceptanceTestExecutionListener : AbstractTestExecutionListener() {

    private val log = LoggerFactory.getLogger(AcceptanceTestExecutionListener::class.java)

    override fun beforeTestClass(testContext: TestContext) {
        val serverPort = testContext.applicationContext.environment
            .getProperty("local.server.port", Int::class.java)
            ?: throw IllegalStateException("localServerPort cannot be null")
        val restDocumentation = testContext.applicationContext.getBean(RestDocumentationContextProvider::class.java)

        log.info { "Setting Started" }
        RestAssured.port = serverPort
        RestAssured.requestSpecification = RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build()

        RestAssured.config = RestAssured.config()
            .logConfig(
                LogConfig.logConfig()
                    .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                    .enablePrettyPrinting(true)
            )
            .encoderConfig(
                EncoderConfig.encoderConfig()
                    .defaultCharsetForContentType(StandardCharsets.UTF_8.name(), ContentType.ANY)
            );
        log.info { "Setting Finished" }
    }

    override fun afterTestClass(testContext: TestContext) {
        RestAssured.reset()
    }

    override fun beforeTestMethod(testContext: TestContext) {
        val jdbcTemplate = testContext.applicationContext.getBean(JdbcTemplate::class.java)
        val objectMapper = testContext.applicationContext.getBean(ObjectMapper::class.java)

        TestContextAnnotationUtils.getMergedRepeatableAnnotations(testContext.testClass, AcceptanceTest::class.java)
            .map { it.setUpScripts }
            .forEach { files ->
                files.forEach { file ->
                    log.info { "$file Setting Started" }
                    setUpDatabase(jdbcTemplate, objectMapper, file)
                }
            }
    }

    private fun setUpDatabase(jdbcTemplate: JdbcTemplate, objectMapper: ObjectMapper, filePath: String) {
        val parsedJsonSql = objectMapper.readValue(
            StreamUtils.copyToString(ClassPathResource(filePath).inputStream, Charset.defaultCharset()),
            Map::class.java
        ) as Map<String, List<Map<String, String>>>
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")

        createInsertQueries(parsedJsonSql).forEach { query ->
            log.info({ "$query Execute" })
            jdbcTemplate.execute(query)
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE")
    }

    private fun createInsertQueries(parsedJsonSql: Map<String, List<Map<String, Any>>>): List<String> {
        return parsedJsonSql.flatMap { (tableName, rows) ->
            rows.map { it.minus("_comment") }.map { row ->
                val columns = row.keys.joinToString(", ")
                val values = row.values.joinToString(", ") { value: Any? ->
                    formatValue(value)
                }
                "INSERT INTO $tableName ($columns) VALUES ($values);"
            }
        }
    }

    private fun formatValue(value: Any?): String {
        return when {
            value == null -> "NULL"
            value is String && value.lowercase() == "now()" -> "now()"
            value is String && value.lowercase() == "uuid()" -> "'${UUID.randomUUID()}'"
            else -> "'$value'"
        }
    }

    override fun afterTestMethod(testContext: TestContext) {
        val jdbcTemplate = testContext.applicationContext.getBean(JdbcTemplate::class.java)
        val truncateQueries = getTruncateQueries(jdbcTemplate)
        truncateTables(jdbcTemplate, truncateQueries)
    }

    private fun getTruncateQueries(jdbcTemplate: JdbcTemplate): List<String> {
        return jdbcTemplate.queryForList(
            "SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
            String::class.java
        )
    }

    private fun truncateTables(jdbcTemplate: JdbcTemplate, truncateQueries: List<String>) {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")
        truncateQueries.forEach { query -> jdbcTemplate.execute(query) }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE")
    }
}
