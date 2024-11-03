plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.home.data)
    api(projects.home.domain)
    api(projects.home.presentation)
}