plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.instance.data)
    api(projects.feature.instance.domain)
    api(projects.feature.instance.presentation)
}