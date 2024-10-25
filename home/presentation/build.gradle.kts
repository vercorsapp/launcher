import app.vercors.launcher.build.Core
import app.vercors.launcher.build.Home
import app.vercors.launcher.build.moduleImpl

plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    moduleImpl(Core.Presentation)
    moduleImpl(Home.Domain)
}