plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    api(projects.core.config)
    api(projects.core.domain)
    api(projects.core.meta)
    api(projects.core.network)
    api(projects.core.presentation)
    api(projects.core.serialization)
    api(projects.core.storage)
}