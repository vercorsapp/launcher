package app.vercors.home

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
val HomeSectionType.title: String
    @Composable get() = stringResource(
        when (this) {
            HomeSectionType.JumpBackIn -> Res.string.jumpBackIn
            HomeSectionType.PopularMods -> Res.string.popularMods
            HomeSectionType.PopularModpacks -> Res.string.popularModpacks
            HomeSectionType.PopularResourcePacks -> Res.string.popularResourcePacks
            HomeSectionType.PopularShaderPacks -> Res.string.popularShaderPacks
        }
    )

@OptIn(ExperimentalResourceApi::class)
val HomeSectionType.shortTitle: String
    @Composable get() = stringResource(
        when (this) {
            HomeSectionType.JumpBackIn -> Res.string.instances
            HomeSectionType.PopularMods -> Res.string.mods
            HomeSectionType.PopularModpacks -> Res.string.modpacks
            HomeSectionType.PopularResourcePacks -> Res.string.resourcePacks
            HomeSectionType.PopularShaderPacks -> Res.string.shaderPacks
        }
    )