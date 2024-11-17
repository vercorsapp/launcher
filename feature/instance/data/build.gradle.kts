plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.feature.instance.domain)
    implementation(projects.feature.game.data)
    implementation(projects.feature.game.domain)
}