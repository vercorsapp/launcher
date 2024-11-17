plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.game.data)
    api(projects.feature.game.domain)
    api(projects.feature.game.presentation)
}