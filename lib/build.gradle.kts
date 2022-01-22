plugins {
    `java-library`
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging")
    implementation("io.r2dbc:r2dbc-spi")
    implementation("io.projectreactor:reactor-core")
    runtimeOnly("dev.miku:r2dbc-mysql")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("io.projectreactor:reactor-test")
}
