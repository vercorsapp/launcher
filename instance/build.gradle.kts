plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.instance.data)
    api(projects.instance.domain)
    api(projects.instance.presentation)
}