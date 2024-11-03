package app.vercors.launcher.core.config.mapper

import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.config.proto.Tab
import org.koin.core.annotation.Single

@Single
class TabConfigMapper {
    fun fromProto(tab: Tab): TabConfig = when (tab) {
        Tab.HOME -> TabConfig.Home
        Tab.INSTANCES -> TabConfig.Instances
        Tab.PROJECTS -> TabConfig.Projects
        Tab.ACCOUNTS -> TabConfig.Accounts
        Tab.SETTINGS -> TabConfig.Settings
        Tab.UNRECOGNIZED -> TabConfig.entries.first()
    }

    fun toProto(tabConfig: TabConfig): Tab = when (tabConfig) {
        TabConfig.Home -> Tab.HOME
        TabConfig.Instances -> Tab.INSTANCES
        TabConfig.Projects -> Tab.PROJECTS
        TabConfig.Accounts -> Tab.ACCOUNTS
        TabConfig.Settings -> Tab.SETTINGS
    }
}
