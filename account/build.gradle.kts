import app.vercors.launcher.build.Account
import app.vercors.launcher.build.moduleApi

plugins {
    id("app.vercors.launcher.base")
}

dependencies {
    moduleApi(Account)
}