import app.vercors.launcher.build.Core
import app.vercors.launcher.build.Game
import app.vercors.launcher.build.moduleImpl

plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    moduleImpl(Core.data)
    moduleImpl(Game.domain)
}