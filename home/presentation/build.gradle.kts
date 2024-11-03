plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    implementation(projects.core.presentation)
    implementation(projects.home.domain)
    implementation(projects.instance.presentation)
    implementation(projects.instance.domain)
    implementation(projects.project.presentation)
    implementation(projects.project.domain)
}