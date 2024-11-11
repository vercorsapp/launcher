package app.vercors.launcher.settings.presentation.ui

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.stringResource

val HomeSectionConfig.displayName: String
    @Composable get() = stringResource(
        when (this) {
            HomeSectionConfig.JumpBackIn -> CoreString.instances
            HomeSectionConfig.PopularMods -> CoreString.mods
            HomeSectionConfig.PopularModpacks -> CoreString.modpacks
            HomeSectionConfig.PopularResourcePacks -> CoreString.resource_packs
            HomeSectionConfig.PopularShaderPacks -> CoreString.shader_packs
        }
    )