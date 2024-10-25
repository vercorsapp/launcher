import app.vercors.launcher.build.Core
import app.vercors.launcher.build.Instance
import app.vercors.launcher.build.moduleImpl

plugins {
    id("app.vercors.launcher.data")
}

dependencies {
    moduleImpl(Core.Data)
    moduleImpl(Instance.Domain)
}