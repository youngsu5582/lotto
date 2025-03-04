plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "2.1.0"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.asciidoctor.jvm.convert") version "4.0.0"
}

group = "camp.nextstep.edu"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot 및 Jackson 의존성
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // 토큰
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //암호화
    implementation("org.mindrot:jbcrypt:0.4")

    // 모니터링 및 로키 로그
    implementation ("io.micrometer:micrometer-registry-prometheus")
    implementation ("com.github.loki4j:loki-logback-appender:1.4.0")

    // 레디스
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")

    // Test 및 REST Docs 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage") // JUnit 5 전용
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("io.rest-assured:rest-assured:5.3.1")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("io.mockk:mockk:1.13.5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    annotationProcessor("org.springframework:spring-context-indexer")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("restDocsTest") {
    useJUnitPlatform {
        includeTags("AcceptanceTest")
    }

    systemProperty("org.springframework.restdocs.outputDir", file("build/generated-snippets"))
}


tasks.register("generateSnippetIndexes") {
    val snippetsDir = file("build/generated-snippets")
    snippetsDir.listFiles { file -> file.isDirectory }?.forEach { snippetFolder ->
        val includeFiles = listOf(
            "http-request.adoc",
            "http-response.adoc",
            "request-fields.adoc",
            "response-fields.adoc"
        )
        val includesContent = includeFiles
            .filter { File(snippetFolder, it).exists() }
            .joinToString("\n") { "include::${it}[]" }
        val indexFile = File(snippetFolder, "index.adoc")
        indexFile.writeText(includesContent)
        println("Generated index.adoc in ${snippetFolder.name}:")
        println(includesContent)
    }
}


tasks.named("asciidoctor", org.asciidoctor.gradle.jvm.AsciidoctorTask::class.java) {
    dependsOn("restDocsTest", "generateSnippetIndexes")
    inputs.dir(file("build/generated-snippets"))
    baseDirFollowsSourceFile()
    attributes(
        mapOf(
            "snippets" to file("build/generated-snippets"),
            "source-highlighter" to "highlightjs",
            "highlightjs-theme" to "github",
            "icons" to "font",
            "sectnums" to true,
            "toc" to "left",
            "toc-title" to "Table of Contents",
            "prewrap" to "true",
            "docinfo1" to true,
            "hardbreaks" to true
        )
    )
    outputOptions {
        backends("html")
    }
}
