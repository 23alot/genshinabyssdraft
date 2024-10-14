pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        google()
        mavenCentral()
    }
}

rootProject.name = "com.alot23.genshinabyssdraft"

include(":ktor")
include(":entity")

project(":entity").projectDir = File(settingsDir, "../entity")