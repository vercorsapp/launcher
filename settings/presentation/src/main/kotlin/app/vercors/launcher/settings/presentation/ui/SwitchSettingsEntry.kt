package app.vercors.launcher.settings.presentation.ui

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.ui.handPointer

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
            modifier = Modifier.handPointer(),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}