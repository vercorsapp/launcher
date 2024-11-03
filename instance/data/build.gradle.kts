plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.instance.domain)
    implementation(projects.game.data)
    implementation(projects.game.domain)
}