import app.vercors.launcher.build.Instance
import app.vercors.launcher.build.moduleApi


plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    moduleApi(Instance)
}