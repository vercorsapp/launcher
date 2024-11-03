plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.game.data)
    api(projects.game.domain)
    api(projects.game.presentation)
}