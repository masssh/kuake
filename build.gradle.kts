import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import me.masssh.kuake.gradle.Versions as V

plugins {
    kotlin("jvm") version me.masssh.kuake.gradle.Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version me.masssh.kuake.gradle.Versions.ktlintPlugin
    id("io.gitlab.arturbosch.detekt") version me.masssh.kuake.gradle.Versions.detekt
    id("io.spring.dependency-management") version me.masssh.kuake.gradle.Versions.dependencyManagement
}

tasks.withType<Wrapper> {
    gradleVersion = V.gradle
}

configure(allprojects) {
    group = "me.masssh.kuake"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "io.spring.dependency-management")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = listOf("-Xjsr305=strict")
            javaParameters = true
        }
    }

    kotlin {
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_11.toString()))
        }
    }

    ktlint {
        version.set(V.ktlint)
    }

    detekt {
        config = this.config.from("$rootDir/detekt-config.yml")
        parallel = true
        buildUponDefaultConfig = true
    }

    tasks.test {
        useJUnitPlatform()
    }

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("io.projectreactor:reactor-bom:${me.masssh.kuake.gradle.Versions.reactor}")
        }
        dependencies {
            dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${V.kotlinxCoroutins}")
            dependency("org.jetbrains.kotlin:kotlin-reflect:${V.kotlin}")
            dependency("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
            dependency("io.r2dbc:r2dbc-spi:${V.r2dbc}")
            dependency("dev.miku:r2dbc-mysql:${V.r2dbc}")
            dependency("org.junit.jupiter:junit-jupiter:${V.junit}")
            dependency("org.assertj:assertj-core:${V.assertj}")
            dependency("ch.qos.logback:logback-classic:${V.logback}")
        }
    }
}
