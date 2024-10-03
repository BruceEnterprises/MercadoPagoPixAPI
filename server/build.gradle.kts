plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "org.example.project"
version = "1.0.0"
application {
    mainClass.set("org.example.project.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
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
