package app.vercors.menu

import app.vercors.common.AppTab

enum class MenuButton(val tab: AppTab?) {
    Home(AppTab.Home),
    Instances(AppTab.Instances),
    Search(AppTab.Search),
    Settings(AppTab.Settings),
    CreateInstance(null),
    Accounts(null);

    companion object {
        fun fromTab(tab: AppTab) = when (tab) {
            AppTab.Home -> Home
            AppTab.Instances -> Instances
            AppTab.Search -> Search
            AppTab.Settings -> Settings
        }
    }
}
