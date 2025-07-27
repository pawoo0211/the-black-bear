plugins {
    id("com.netflix.dgs.codegen")
    id("com.github.node-gradle.node") version "7.1.0"
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring") // ← spring 관련 확장을 위해 필요
    kotlin("jvm")
}

group = "com.blackbear.business"
version = "0.0.1-SNAPSHOT"

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    // implementation("org.springframework.boot:spring-boot-starter-logging")

    // GraphQL / Dgs
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("com.netflix.graphql.dgs:dgs-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-pagination")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-micrometer")
    implementation("com.graphql-java:graphql-java-extended-validation")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("ch.qos.logback:logback-classic")
    implementation("org.slf4j:slf4j-api")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("io.github.openfeign:feign-okhttp")

    // Spring Batch
    // implementation("org.springframework.boot:spring-boot-starter-batch")
    // implementation("org.jetbrains.exposed:exposed-spring-boot-starter")
    // implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("com.h2database:h2")
//    implementation("org.jetbrains.exposed:exposed-core")
//    implementation("org.jetbrains.exposed:exposed-dao")
//    implementation("org.jetbrains.exposed:exposed-jdbc")
//    implementation("org.jetbrains.exposed:exposed-java-time")
//    implementation("org.mariadb.jdbc:mariadb-java-client")
//    implementation("org.apache.commons:commons-dbcp2")
//    implementation("org.postgresql:postgresql")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5") // Kotest JUnit5 integration
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-property")

    // mockk
    testImplementation("io.mockk:mockk")

    // testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
}

@OptIn(ExperimentalStdlibApi::class)
tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    language = "KOTLIN"
    schemaPaths =
            mutableListOf(
                    "$projectDir/src/main/resources/graphql",
            )
    packageName = "com.commerce.proxy.shopping.infrastructure.dto" // The package name to use to generate sources
    typeMapping =
            mutableMapOf(
                    "Long" to "kotlin.Long",
                    "BigDecimal" to "java.math.BigDecimal",
            )
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.blackbear.business.BusinessApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}
