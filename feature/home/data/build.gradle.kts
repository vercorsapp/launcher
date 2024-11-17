plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.config)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.project.domain)
    implementation(projects.feature.instance.domain)
}