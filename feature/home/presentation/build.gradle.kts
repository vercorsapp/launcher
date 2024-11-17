plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    implementation(projects.core.presentation)
    implementation(projects.core.resources)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.instance.presentation)
    implementation(projects.feature.instance.domain)
    implementation(projects.feature.project.presentation)
    implementation(projects.feature.project.domain)
    implementation(projects.feature.game.domain)
}