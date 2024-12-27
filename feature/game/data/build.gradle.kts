plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.feature.game.domain)
    implementation(projects.core.meta)
    implementation(projects.core.domain)
}