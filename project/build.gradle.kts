import app.vercors.launcher.build.Project
import app.vercors.launcher.build.moduleApi

plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    moduleApi(Project)
}