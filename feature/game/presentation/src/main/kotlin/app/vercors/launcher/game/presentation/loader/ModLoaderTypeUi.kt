package app.vercors.launcher.game.presentation.loader

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.game.domain.loader.ModLoaderType

val ModLoaderType?.displayName: String
    @Composable get() = appStringResource {
        when (this@displayName) {
            ModLoaderType.Forge -> forge
            ModLoaderType.Fabric -> fabric
            ModLoaderType.Neoforge -> neoforge
            ModLoaderType.Quilt -> quilt
            null -> vanilla
        }
    }

val ModLoaderType?.icon: ImageVector
    @Composable get() = appVectorResource {
        when (this@icon) {
            ModLoaderType.Forge -> forge
            ModLoaderType.Fabric -> fabric
            ModLoaderType.Neoforge -> neoforge
            ModLoaderType.Quilt -> quilt
            null -> feather
        }
    }