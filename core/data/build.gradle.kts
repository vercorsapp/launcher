import app.vercors.launcher.build.*

plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.ktor.client.cio)
}