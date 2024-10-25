import app.vercors.launcher.build.Home
import app.vercors.launcher.build.moduleApi

plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    moduleApi(Home)
}