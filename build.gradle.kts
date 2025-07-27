plugins {
    val kotlinVersion = "2.0.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion apply false
    id("org.springframework.boot") version "3.4.4" apply false
    id("io.spring.dependency-management") version "1.1.7"
    id("com.netflix.dgs.codegen") version "8.0.4" apply false
    id("org.asciidoctor.jvm.convert") version "4.0.4" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

group = "com.blackbare"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://packages.confluent.io/maven/")
        maven(url = "https://jitpack.io/")
        gradlePluginPortal()
        mavenLocal()
        maven(url = uri("https://repo.spring.io/milestone"))
        maven(url = uri("https://repo.spring.io/snapshot"))
        maven("https://repository.pentaho.org/artifactory/repo")
        maven("https://packages.confluent.io/maven")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencyManagement {
        dependencies {
            // mariadb , mysql
            dependency("org.mariadb.jdbc:mariadb-java-client:3.5.3")
            dependency("mysql:mysql-connector-java:8.0.33")

            // postgresql
            dependency("org.postgresql:postgresql:42.7.5")

            dependency("com.graphql-java-kickstart:graphql-java-tools:14.0.0")
            dependency("com.graphql-java-kickstart:graphql-java-extended-scalars:22.0")

            val logbackVersion = "1.5.18"
            dependency("ch.qos.logback:logback-core:$logbackVersion")
            dependency("ch.qos.logback:logback-classic:$logbackVersion")
            dependency("org.slf4j:slf4j-api:2.0.17")

            dependency("org.redisson:redisson-spring-boot-starter:3.45.1")

            val restdocVersion = "2.0.5.RELEASE"
            dependency("org.springframework.restdocs:spring-restdocs:$restdocVersion")

            val restassuredVersion = "4.3.3"
            dependency("io.rest-assured:spring-mock-mvc:$restassuredVersion")
            dependency("io.rest-assured:rest-assured:$restassuredVersion")

            // Kotest dependencies
            val kotestVersion = "5.9.1"
            dependency("io.kotest:kotest-runner-junit5:$kotestVersion") // Kotest JUnit5 integration
            dependency("io.kotest:kotest-assertions-core:$kotestVersion")
            dependency("io.kotest:kotest-property:$kotestVersion")

            // mockk
            dependency("io.mockk:mockk:1.14.2")
        }

        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.jetbrains.exposed:exposed-bom:0.61.0")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
            mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:10.0.4")
            mavenBom("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.6.4")
            mavenBom("io.micrometer:micrometer-tracing-bom:1.4.4")
        }
    }
}
