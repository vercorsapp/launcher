plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.home.data)
    api(projects.feature.home.domain)
    api(projects.feature.home.presentation)
}