plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    implementation(projects.core.presentation)
    implementation(projects.feature.project.domain)
    implementation(projects.feature.game.presentation)
}