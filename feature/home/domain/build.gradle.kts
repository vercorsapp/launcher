plugins {
    id("app.vercors.launcher.domain")
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.instance.domain)
    implementation(projects.feature.project.domain)
}