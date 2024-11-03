plugins {
    id("app.vercors.launcher.domain")
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.game.domain)
}