plugins {
    kotlin("jvm") version me.masssh.kuake.gradle.Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version me.masssh.kuake.gradle.Versions.ktlint
    id("io.gitlab.arturbosch.detekt") version me.masssh.kuake.gradle.Versions.detekt
    id("io.spring.dependency-management") version me.masssh.kuake.gradle.Versions.dependencyManagement
}

configure(subprojects) {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "io.spring.dependency-management")

    val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }

    val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }

    dependencyManagement {
        imports {
            mavenBom("io.projectreactor:reactor-bom:${me.masssh.kuake.gradle.Versions.reactor}")
        }
        dependencies {
            dependency("io.github.microutils:kotlin-logging:${me.masssh.kuake.gradle.Versions.kotlinLogging}")
            dependency("io.r2dbc:r2dbc-spi:${me.masssh.kuake.gradle.Versions.r2dbc}")
            dependency("dev.miku:r2dbc-mysql:${me.masssh.kuake.gradle.Versions.r2dbc}")
            dependency("org.junit.jupiter:junit-jupiter:${me.masssh.kuake.gradle.Versions.junit}")
            dependency("org.assertj:assertj-core:${me.masssh.kuake.gradle.Versions.assertj}")
            dependency("ch.qos.logback:logback-classic:${me.masssh.kuake.gradle.Versions.logback}")
        }
    }

    repositories {
        mavenCentral()
    }
}
