import app.vercors.launcher.build.Account
import app.vercors.launcher.build.Core
import app.vercors.launcher.build.moduleImpl

plugins {
    id("app.vercors.launcher.presentation")
}

dependencies {
    moduleImpl(Core.presentation)
    moduleImpl(Account.domain)
}