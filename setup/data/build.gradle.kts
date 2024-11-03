plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.storage)
    implementation(projects.setup.domain)
}