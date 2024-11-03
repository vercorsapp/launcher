plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.account.data)
    api(projects.account.domain)
    api(projects.account.presentation)
}