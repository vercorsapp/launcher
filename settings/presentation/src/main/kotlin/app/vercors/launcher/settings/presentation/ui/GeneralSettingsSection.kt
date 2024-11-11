package app.vercors.launcher.settings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.presentation.theme.LocalCatppuccinColors
import app.vercors.launcher.settings.generated.resources.*
import app.vercors.launcher.settings.presentation.SettingsString
import app.vercors.launcher.settings.presentation.ui.entry.ComboboxSettingsEntry
import app.vercors.launcher.settings.presentation.ui.entry.SwitchSettingsEntry
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiIntent
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun GeneralSettingsSection(
    state: SettingsUiState.Loaded,
    onIntent: (SettingsUiIntent) -> Unit
) {
    SettingsSection(
        title = stringResource(SettingsString.general)
    ) {
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.theme),
            description = stringResource(SettingsString.theme_description),
            options = state.themes,
            value = state.currentTheme,
            textConverter = { it.name },
            onValueChange = { onIntent(SettingsUiIntent.SelectTheme(it.id)) }
        ) {
            Text(text = it.name)
        }
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.accent),
            description = stringResource(SettingsString.accent_description),
            options = state.accentColors,
            value = state.currentAccentColor,
            textConverter = { it.name },
            leadingIcon = { ColorSquare(MaterialTheme.colorScheme.primary) },
            onValueChange = { onIntent(SettingsUiIntent.SelectAccent(it.id)) }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ColorSquare(LocalCatppuccinColors.current.run(it.color))
                Text(text = it.name)
            }
        }
        SwitchSettingsEntry(
            title = stringResource(SettingsString.gradient),
            description = stringResource(SettingsString.gradient_description),
            checked = state.config.general.gradient,
            onCheckedChange = { onIntent(SettingsUiIntent.ToggleGradient(it)) }
        )
        SwitchSettingsEntry(
            title = stringResource(SettingsString.decorated),
            description = stringResource(SettingsString.decorated_description),
            checked = state.config.general.decorated,
            onCheckedChange = { onIntent(SettingsUiIntent.ToggleDecorated(it)) }
        )
        SwitchSettingsEntry(
            title = stringResource(SettingsString.animations),
            description = stringResource(SettingsString.animations_description),
            checked = state.config.general.animations,
            onCheckedChange = { onIntent(SettingsUiIntent.ToggleAnimations(it)) }
        )
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.default_tab),
            description = stringResource(SettingsString.default_tab_description),
            options = TabConfig.entries,
            value = state.config.general.defaultTab,
            textConverter = { it.displayName },
            leadingIcon = {
                Icon(
                    imageVector = state.config.general.defaultTab.icon,
                    contentDescription = state.config.general.defaultTab.displayName
                )
            },
            onValueChange = { onIntent(SettingsUiIntent.SelectDefaultTab(it)) }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = it.icon,
                    contentDescription = it.displayName
                )
                Text(text = it.displayName)
            }
        }
    }
}