plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.alot23"
version = "0.0.1"

application {
    mainClass.set("com.alot23.genshinabyssdraft.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.websockets)
    implementation(libs.logback)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlin.test)
    implementation(project(":entity"))
}
