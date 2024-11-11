plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.config)
    implementation(projects.home.domain)
    implementation(projects.project.domain)
    implementation(projects.instance.domain)
}