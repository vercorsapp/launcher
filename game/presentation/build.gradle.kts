import app.vercors.launcher.build.Core
import app.vercors.launcher.build.Game
import app.vercors.launcher.build.moduleImpl

plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    moduleImpl(Core.presentation)
    moduleImpl(Game.domain)
}