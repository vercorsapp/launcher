plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.setup.data)
    api(projects.feature.setup.domain)
    api(projects.feature.setup.presentation)
}