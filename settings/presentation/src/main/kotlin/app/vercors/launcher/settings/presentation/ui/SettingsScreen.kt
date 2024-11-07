package app.vercors.launcher.settings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import app.vercors.launcher.settings.presentation.action.SettingsAction
import app.vercors.launcher.settings.presentation.state.SettingsUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
) {
    if (state is SettingsUiState.Loaded) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SettingsSection(
                title = stringResource(SettingsString.general)
            ) {
                ComboboxSettingsEntry(
                    title = stringResource(SettingsString.theme),
                    description = stringResource(SettingsString.theme_description),
                    options = state.themes,
                    value = state.currentTheme,
                    textConverter = { it.name },
                    onValueChange = { onAction(SettingsAction.SelectTheme(it.id)) }
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
                    onValueChange = { onAction(SettingsAction.SelectAccent(it.id)) }
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
                    title = stringResource(SettingsString.decorated),
                    description = stringResource(SettingsString.decorated_description),
                    checked = state.config.general.decorated,
                    onCheckedChange = { onAction(SettingsAction.ToggleDecorated(it)) }
                )
                SwitchSettingsEntry(
                    title = stringResource(SettingsString.animations),
                    description = stringResource(SettingsString.animations_description),
                    checked = state.config.general.animations,
                    onCheckedChange = { onAction(SettingsAction.ToggleAnimations(it)) }
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
                    onValueChange = { onAction(SettingsAction.SelectDefaultTab(it)) }
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
    }
}
