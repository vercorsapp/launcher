package app.vercors.launcher.home.presentation.ui.project

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.HomeProjectCardBox(
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier.weight(1f).aspectRatio(1.5f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        content()
    }
}