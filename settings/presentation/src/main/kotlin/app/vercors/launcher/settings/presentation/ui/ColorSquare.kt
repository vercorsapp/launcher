package app.vercors.launcher.settings.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorSquare(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(16.dp).background(color)
    )
}