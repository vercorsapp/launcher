package app.vercors.launcher.settings.presentation.entry

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.ui.AppDropdownMenuBox

@Composable
@Suppress("kotlin:S107")
fun <T> ComboboxSettingsEntry(
    title: String,
    description: String,
    options: Iterable<T>,
    value: T?,
    textConverter: @Composable (T) -> String,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier.width(IntrinsicSize.Max),
    leadingIcon: @Composable (() -> Unit)? = null,
    optionContent: @Composable (T) -> Unit
) {
    SettingsEntry(
        title = title,
        description = description
    ) {
        AppDropdownMenuBox(
            options = options,
            value = value,
            textConverter = textConverter,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            modifier = modifier,
            optionContent = optionContent
        )
    }
}