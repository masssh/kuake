import me.masssh.kuake.gradle.Versions as V

plugins {
    kotlin("jvm") version me.masssh.kuake.gradle.Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version me.masssh.kuake.gradle.Versions.ktlint
    id("io.gitlab.arturbosch.detekt") version me.masssh.kuake.gradle.Versions.detekt
    id("io.spring.dependency-management") version me.masssh.kuake.gradle.Versions.dependencyManagement
    `java-library`
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:${V.reactor}")
    }
    dependencies {
        dependency("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
        dependency("io.r2dbc:r2dbc-spi:${V.r2dbc}")
        dependency("dev.miku:r2dbc-mysql:${V.r2dbc}")
        dependency("org.junit.jupiter:junit-jupiter:${V.junit}")
        dependency("org.assertj:assertj-core:${V.assertj}")
        dependency("ch.qos.logback:logback-classic:${V.logback}")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging")
    implementation("io.r2dbc:r2dbc-spi")
    implementation("io.projectreactor:reactor-core")
    runtimeOnly("dev.miku:r2dbc-mysql")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("ch.qos.logback:logback-classic")
}
