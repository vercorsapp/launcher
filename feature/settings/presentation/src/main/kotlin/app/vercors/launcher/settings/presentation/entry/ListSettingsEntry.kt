package app.vercors.launcher.settings.presentation.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ListSettingsEntry(
    title: String,
    description: String,
    entries: List<T>,
    selectedItems: List<T>,
    entryContent: @Composable RowScope.(T, Boolean) -> Unit
) {
    SettingsEntry(
        title = title,
        description = description
    ) {
        FlowRow(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(-(10.dp)),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
        ) {
            entries.forEach { item ->
                entryContent(item, item in selectedItems)
            }
        }
    }
}