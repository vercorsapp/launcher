plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.home.domain)
}