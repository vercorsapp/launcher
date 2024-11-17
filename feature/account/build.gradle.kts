plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.feature.account.data)
    api(projects.feature.account.domain)
    api(projects.feature.account.presentation)
}