plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.feature.project.domain)
    implementation(projects.feature.game.data)
}