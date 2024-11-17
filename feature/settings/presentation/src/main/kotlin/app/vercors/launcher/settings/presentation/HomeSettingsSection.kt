package app.vercors.launcher.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.ui.AppFilterChip
import app.vercors.launcher.settings.generated.resources.provider
import app.vercors.launcher.settings.generated.resources.provider_description
import app.vercors.launcher.settings.generated.resources.sections
import app.vercors.launcher.settings.generated.resources.sections_description
import app.vercors.launcher.settings.presentation.entry.ComboboxSettingsEntry
import app.vercors.launcher.settings.presentation.entry.ListSettingsEntry
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeSettingsSection(
    config: HomeConfigUi,
    onIntent: (SettingsUiIntent) -> Unit
) {
    SettingsSection(title = stringResource(CoreString.home)) {
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.provider),
            description = stringResource(SettingsString.provider_description),
            options = HomeProviderConfig.entries,
            value = config.provider,
            textConverter = { it.displayName },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = config.provider.icon,
                    contentDescription = config.provider.displayName
                )
            },
            onValueChange = { onIntent(SettingsUiIntent.SelectProvider(it)) }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = it.icon,
                    contentDescription = it.displayName
                )
                Text(text = it.displayName)
            }
        }
        ListSettingsEntry(
            title = stringResource(SettingsString.sections),
            description = stringResource(SettingsString.sections_description),
            entries = HomeSectionConfig.entries,
            selectedItems = config.sections
        ) { item, selected ->
            AppFilterChip(
                selected = selected,
                onClick = { onIntent(SettingsUiIntent.ToggleSection(item)) },
                label = { Text(text = item.displayName) }
            )
        }
    }
}

private val HomeProviderConfig.displayName: String
    @Composable get() = stringResource(
        when (this) {
            HomeProviderConfig.Modrinth -> CoreString.modrinth
            HomeProviderConfig.Curseforge -> CoreString.curseforge
        }
    )

private val HomeProviderConfig.icon: ImageVector
    @Composable get() = vectorResource(
        when (this) {
            HomeProviderConfig.Modrinth -> CoreDrawable.modrinth
            HomeProviderConfig.Curseforge -> CoreDrawable.curseforge
        }
    )

private val HomeSectionConfig.displayName: String
    @Composable get() = stringResource(
        when (this) {
            HomeSectionConfig.JumpBackIn -> CoreString.instances
            HomeSectionConfig.PopularMods -> CoreString.mods
            HomeSectionConfig.PopularModpacks -> CoreString.modpacks
            HomeSectionConfig.PopularResourcePacks -> CoreString.resource_packs
            HomeSectionConfig.PopularShaderPacks -> CoreString.shader_packs
        }
    )