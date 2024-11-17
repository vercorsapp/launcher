plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.project.data)
    api(projects.feature.project.domain)
    api(projects.feature.project.presentation)
}