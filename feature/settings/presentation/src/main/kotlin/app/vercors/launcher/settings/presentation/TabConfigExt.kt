package app.vercors.launcher.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.resources.*

val TabConfig.displayName: String
    @Composable get() = appStringResource {
        when (this@displayName) {
            TabConfig.Home -> home
            TabConfig.Instances -> instances
            TabConfig.Projects -> projects
            TabConfig.Accounts -> accounts
            TabConfig.Settings -> settings
        }
    }

val TabConfig.icon: ImageVector
    @Composable get() = appVectorResource {
        when (this@icon) {
            TabConfig.Home -> house
            TabConfig.Instances -> hard_drive
            TabConfig.Projects -> folder_cog
            TabConfig.Accounts -> user
            TabConfig.Settings -> settings
        }
    }