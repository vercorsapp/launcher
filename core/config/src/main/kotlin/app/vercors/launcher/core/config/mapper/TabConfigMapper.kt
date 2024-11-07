package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.config.proto.TabProto

fun TabProto.toConfig(): TabConfig = when (this) {
    TabProto.HOME -> TabConfig.Home
    TabProto.INSTANCES -> TabConfig.Instances
    TabProto.PROJECTS -> TabConfig.Projects
    TabProto.ACCOUNTS -> TabConfig.Accounts
    TabProto.SETTINGS -> TabConfig.Settings
    TabProto.UNRECOGNIZED -> TabConfig.entries.first()
}

fun TabConfig.toProto(): TabProto = when (this) {
    TabConfig.Home -> TabProto.HOME
    TabConfig.Instances -> TabProto.INSTANCES
    TabConfig.Projects -> TabProto.PROJECTS
    TabConfig.Accounts -> TabProto.ACCOUNTS
    TabConfig.Settings -> TabProto.SETTINGS
}