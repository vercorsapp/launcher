plugins {
    id("app.vercors.launcher.domain")
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.instance.domain)
    implementation(projects.project.domain)
}