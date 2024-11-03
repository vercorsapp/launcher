plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.project.data)
    api(projects.project.domain)
    api(projects.project.presentation)
}