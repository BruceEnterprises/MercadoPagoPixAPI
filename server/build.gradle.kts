import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.appengine") version "2.8.0"
}

group = "org.example.project"
version = "1.2.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation("com.mercadopago:sdk-java:2.1.29")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.3")
    implementation("io.ktor:ktor-serialization-gson:2.0.3")
}
