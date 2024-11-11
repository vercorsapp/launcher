package app.vercors.launcher.settings.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.generated.resources.curseforge
import app.vercors.launcher.core.generated.resources.modrinth
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

val HomeProviderConfig.displayName: String
    @Composable get() = stringResource(
        when (this) {
            HomeProviderConfig.Modrinth -> CoreString.modrinth
            HomeProviderConfig.Curseforge -> CoreString.curseforge
        }
    )

val HomeProviderConfig.icon: ImageVector
    @Composable get() = vectorResource(
        when (this) {
            HomeProviderConfig.Modrinth -> CoreDrawable.modrinth
            HomeProviderConfig.Curseforge -> CoreDrawable.curseforge
        }
    )