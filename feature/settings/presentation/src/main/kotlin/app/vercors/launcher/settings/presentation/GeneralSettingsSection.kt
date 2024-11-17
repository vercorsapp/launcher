package app.vercors.launcher.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.presentation.theme.LocalCatppuccinColors
import app.vercors.launcher.settings.generated.resources.*
import app.vercors.launcher.settings.presentation.entry.ComboboxSettingsEntry
import app.vercors.launcher.settings.presentation.entry.SwitchSettingsEntry
import org.jetbrains.compose.resources.stringResource

@Composable
fun GeneralSettingsSection(
    config: GeneralConfigUi,
    onIntent: (SettingsUiIntent) -> Unit
) {
    SettingsSection(title = stringResource(SettingsString.general)) {
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.theme),
            description = stringResource(SettingsString.theme_description),
            options = config.themes,
            value = config.currentTheme,
            textConverter = { it.name },
            onValueChange = { onIntent(SettingsUiIntent.SelectTheme(it.id)) }
        ) {
            Text(text = it.name)
        }
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.accent),
            description = stringResource(SettingsString.accent_description),
            options = config.accentColors,
            value = config.currentAccentColor,
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
            checked = config.gradient,
            onCheckedChange = { onIntent(SettingsUiIntent.ToggleGradient(it)) }
        )
        SwitchSettingsEntry(
            title = stringResource(SettingsString.decorated),
            description = stringResource(SettingsString.decorated_description),
            checked = config.decorated,
            onCheckedChange = { onIntent(SettingsUiIntent.ToggleDecorated(it)) }
        )
        SwitchSettingsEntry(
            title = stringResource(SettingsString.animations),
            description = stringResource(SettingsString.animations_description),
            checked = config.animations,
            onCheckedChange = { onIntent(SettingsUiIntent.ToggleAnimations(it)) }
        )
        ComboboxSettingsEntry(
            title = stringResource(SettingsString.default_tab),
            description = stringResource(SettingsString.default_tab_description),
            options = TabConfig.entries,
            value = config.defaultTab,
            textConverter = { it.displayName },
            leadingIcon = {
                Icon(
                    imageVector = config.defaultTab.icon,
                    contentDescription = config.defaultTab.displayName
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

@Composable
private fun ColorSquare(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(16.dp).background(color)
    )
}