plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    implementation(projects.core.presentation)
    implementation(projects.core.resources)
    implementation(projects.feature.game.domain)
}