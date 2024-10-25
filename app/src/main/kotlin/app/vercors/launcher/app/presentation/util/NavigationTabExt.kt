package app.vercors.launcher.app.presentation.util

import app.vercors.launcher.app.presentation.state.NavigationTab
import app.vercors.launcher.core.presentation.navigation.AppDestination

val NavigationTab.defaultDestination: AppDestination
    get() = when (this) {
        NavigationTab.Home -> AppDestination.Home
        NavigationTab.Instances -> AppDestination.InstanceList
        NavigationTab.Projects -> AppDestination.ProjectList
        NavigationTab.Accounts -> AppDestination.Accounts
        NavigationTab.Settings -> AppDestination.Settings
    }