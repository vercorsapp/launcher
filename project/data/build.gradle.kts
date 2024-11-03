plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.project.domain)
    implementation(projects.game.data)
}