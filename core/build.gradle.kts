import app.vercors.launcher.build.Core
import app.vercors.launcher.build.moduleApi

plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    moduleApi(Core)
}