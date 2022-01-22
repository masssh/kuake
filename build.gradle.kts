import me.masssh.kuake.gradle.Versions as V

plugins {
    id("io.spring.dependency-management") version me.masssh.kuake.gradle.Versions.dependencyManagement
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
        dependency("dev.miku:r2dbc-mysql:${V.r2dbcMysql}")
        dependency("org.junit.jupiter:junit-jupiter:${V.junit}")
        dependency("org.assertj:assertj-core:${V.assertj}")
        dependency("ch.qos.logback:logback-classic:${V.logback}")
    }
}
