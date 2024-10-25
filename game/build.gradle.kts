import app.vercors.launcher.build.Game
import app.vercors.launcher.build.moduleApi

plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    moduleApi(Game)
}