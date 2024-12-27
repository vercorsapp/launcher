plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.account)
    api(projects.feature.game)
    api(projects.feature.home)
    api(projects.feature.instance)
    api(projects.feature.project)
    api(projects.feature.settings)
    api(projects.feature.setup)
}