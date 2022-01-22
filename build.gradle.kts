import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {}

plugins {
    // kotlin
    kotlin("jvm") version "1.5.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC1"

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

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.12.0")
    testImplementation("ch.qos.logback:logback-classic:1.2.10")
}
