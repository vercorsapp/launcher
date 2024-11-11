import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("app.vercors.launcher.base")
    id("de.jensklingenberg.ktorfit")
    id("androidx.room")
}

val Project.libs get() = the<LibrariesForLibs>()

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktorfit.lib.light)
    implementation(libs.ktorfit.converters.flow)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.sqlite.bundled)
    implementation(libs.appdirs)
}

room {
    schemaDirectory("$projectDir/schemas")
}