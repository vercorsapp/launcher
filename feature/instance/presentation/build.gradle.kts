plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.resources)
    implementation(projects.feature.instance.domain)
    implementation(projects.feature.game.presentation)
    implementation(projects.feature.game.domain)
}