plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    implementation(projects.core.presentation)
    implementation(projects.core.resources)
    implementation(projects.core.config)
    implementation(projects.core.domain)
    implementation(projects.feature.setup.domain)
}