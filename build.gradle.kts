import me.masssh.kuake.gradle.Versions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // kotlin
    kotlin("jvm") version Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlint
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
    id("io.spring.dependency-management") version Versions.dependencyManagement
    `java-library`
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
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
        mavenBom("io.projectreactor:reactor-bom:${Versions.reactor}")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:${Versions.kotlinLogging}")
    runtimeOnly("dev.miku:r2dbc-mysql:${Versions.r2dbcMysql}")
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit}")
    testImplementation("org.assertj:assertj-core:${Versions.assertj}")
    testImplementation("ch.qos.logback:logback-classic:${Versions.logback}")
}
