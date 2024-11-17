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
import app.vercors.launcher.core.presentation.ui.AppFilterChip
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.settings.presentation.entry.ComboboxSettingsEntry
import app.vercors.launcher.settings.presentation.entry.ListSettingsEntry

@Composable
fun HomeSettingsSection(
    config: HomeConfigUi,
    onIntent: (SettingsUiIntent) -> Unit
) {
    SettingsSection(title = appStringResource { home }) {
        ComboboxSettingsEntry(
            title = appStringResource { provider },
            description = appStringResource { provider_description },
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
            title = appStringResource { sections },
            description = appStringResource { sections_description },
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
    @Composable get() = appStringResource {
        when (this@displayName) {
            HomeProviderConfig.Modrinth -> modrinth
            HomeProviderConfig.Curseforge -> curseforge
        }
    }

private val HomeProviderConfig.icon: ImageVector
    @Composable get() = appVectorResource {
        when (this@icon) {
            HomeProviderConfig.Modrinth -> modrinth
            HomeProviderConfig.Curseforge -> curseforge
        }
    }

private val HomeSectionConfig.displayName: String
    @Composable get() = appStringResource {
        when (this@displayName) {
            HomeSectionConfig.JumpBackIn -> instances
            HomeSectionConfig.PopularMods -> mods
            HomeSectionConfig.PopularModpacks -> modpacks
            HomeSectionConfig.PopularResourcePacks -> resource_packs
            HomeSectionConfig.PopularShaderPacks -> shader_packs
        }
    }