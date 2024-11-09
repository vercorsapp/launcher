package app.vercors.launcher.app.util

import app.vercors.launcher.app.navigation.AppDestination
import app.vercors.launcher.app.state.NavigationTab

val NavigationTab.defaultDestination: AppDestination
    get() = when (this) {
        NavigationTab.Home -> AppDestination.Home
        NavigationTab.Instances -> AppDestination.InstanceList
        NavigationTab.Projects -> AppDestination.ProjectList
        NavigationTab.Accounts -> AppDestination.Accounts
        NavigationTab.Settings -> AppDestination.Settings
    }