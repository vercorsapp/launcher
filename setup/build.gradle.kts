plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.setup.data)
    api(projects.setup.domain)
    api(projects.setup.presentation)
}