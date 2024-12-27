package app.vercors.launcher.home.presentation.instance

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.card.AppElevatedCard

@Composable
fun RowScope.HomeInstanceCardBox(
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    AppElevatedCard(
        onClick = onClick,
        modifier = modifier.weight(1f).aspectRatio(0.7f)
    ) {
        content()
    }
}

@Composable
fun RowScope.HomeInstanceCardBox(
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    AppElevatedCard(modifier = modifier.weight(1f).aspectRatio(0.7f)) {
        content()
    }
}