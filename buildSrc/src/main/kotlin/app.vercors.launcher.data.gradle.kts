import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("app.vercors.launcher.base")
}

val Project.libs get() = the<LibrariesForLibs>()

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}