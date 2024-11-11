package app.vercors.launcher.settings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.generated.resources.home
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.ui.AppFilterChip
import app.vercors.launcher.settings.generated.resources.provider
import app.vercors.launcher.settings.generated.resources.provider_description
import app.vercors.launcher.settings.generated.resources.sections
import app.vercors.launcher.settings.generated.resources.sections_description
import app.vercors.launcher.settings.presentation.SettingsString
import app.vercors.launcher.settings.presentation.ui.entry.ComboboxSettingsEntry
import app.vercors.launcher.settings.presentation.ui.entry.OrderableListSettingsEntry
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiIntent
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeSettingsSection(
    state: SettingsUiState.Loaded,
    onIntent: (SettingsUiIntent) -> Unit
) {
    SettingsSection(
        title = stringResource(CoreString.home)
    ) {
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.provider),
            description = stringResource(SettingsString.provider_description),
            options = HomeProviderConfig.entries,
            value = state.config.home.provider,
            textConverter = { it.displayName },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = state.config.home.provider.icon,
                    contentDescription = state.config.home.provider.displayName
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
        OrderableListSettingsEntry(
            title = stringResource(SettingsString.sections),
            description = stringResource(SettingsString.sections_description),
            entries = HomeSectionConfig.entries,
            selectedItems = state.config.home.sections
        ) { item, selected ->
            AppFilterChip(
                selected = selected,
                onClick = { onIntent(SettingsUiIntent.ToggleSection(item)) },
                label = { Text(text = item.displayName) },
            )
        }
    }
}