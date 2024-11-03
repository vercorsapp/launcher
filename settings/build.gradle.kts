plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.settings.data)
    api(projects.settings.domain)
    api(projects.settings.presentation)
}