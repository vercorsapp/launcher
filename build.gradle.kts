plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.mockposable) apply false
}

group = "app.vercors"
version = "0.1.0"

subprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}
