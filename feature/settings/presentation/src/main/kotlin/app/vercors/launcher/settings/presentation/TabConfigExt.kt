package app.vercors.launcher.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

val TabConfig.displayName: String
    @Composable get() = stringResource(
        when (this) {
            TabConfig.Home -> CoreString.home
            TabConfig.Instances -> CoreString.instances
            TabConfig.Projects -> CoreString.projects
            TabConfig.Accounts -> CoreString.accounts
            TabConfig.Settings -> CoreString.settings
        }
    )

val TabConfig.icon: ImageVector
    @Composable get() = vectorResource(
        when (this) {
            TabConfig.Home -> CoreDrawable.house
            TabConfig.Instances -> CoreDrawable.hard_drive
            TabConfig.Projects -> CoreDrawable.folder_cog
            TabConfig.Accounts -> CoreDrawable.user
            TabConfig.Settings -> CoreDrawable.settings
        }
    )