package app.vercors.launcher.settings.presentation.ui

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable

@Composable
fun SwitchSettingsEntry(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsEntry(
        title = title,
        description = description
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}