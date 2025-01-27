package config

import app.LottoApplication
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.AliasFor
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners

@Retention(AnnotationRetention.RUNTIME)
@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [LottoApplication::class],
    properties = ["spring.profiles.active=test"]
)
@AutoConfigureObservability
@TestExecutionListeners(
    value = [AcceptanceTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@AutoConfigureRestDocs
@Import(*[AcceptanceConfig::class])
annotation class AcceptanceTest(
    @get:AliasFor("setUpScripts")
    val value: Array<String> = [],
    val setUpScripts: Array<String> = []
)
