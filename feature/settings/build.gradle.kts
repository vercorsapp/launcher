plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.settings.data)
    api(projects.feature.settings.domain)
    api(projects.feature.settings.presentation)
}