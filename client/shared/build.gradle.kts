@file:Suppress("OPT_IN_USAGE")

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "wasmApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                        add(project.projectDir.path + "/commonMain/")
                        add(project.projectDir.path + "/wasmJsMain/")
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.animation)
                implementation(libs.kermit)
                api(libs.precompose)
                api(libs.precompose.viewmodel)
                implementation(libs.koin.core)
                implementation(libs.serialization)
                implementation(compose.components.resources)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                api(projects.entity)
            }
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(libs.ktor.client.js)
            }
        }
    }
}

android {
    namespace = "com.alot23.genshinabyssdraft"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "com.alot23.genshinabyssdraft.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.alot23.genshinabyssdraft"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}