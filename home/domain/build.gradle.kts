import app.vercors.launcher.build.Core
import app.vercors.launcher.build.Instance
import app.vercors.launcher.build.Project
import app.vercors.launcher.build.moduleImpl

plugins {
    id("app.vercors.launcher.domain")
}

dependencies {
    moduleImpl(Core.domain)
    moduleImpl(Instance.domain)
    moduleImpl(Project.domain)
}