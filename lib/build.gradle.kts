import me.masssh.kuake.gradle.Versions as V

plugins {
    kotlin("jvm") version me.masssh.kuake.gradle.Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version me.masssh.kuake.gradle.Versions.ktlint
    id("io.gitlab.arturbosch.detekt") version me.masssh.kuake.gradle.Versions.detekt
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

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
    runtimeOnly("dev.miku:r2dbc-mysql:${V.r2dbcMysql}")
    testImplementation("org.junit.jupiter:junit-jupiter:${V.junit}")
    testImplementation("org.assertj:assertj-core:${V.assertj}")
    testImplementation("ch.qos.logback:logback-classic:${V.logback}")
}
