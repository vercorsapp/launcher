package app.vercors.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.vercors.applyIf

@Composable
fun OutlinedFieldButton(
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    size: Dp = 55.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.size(size)
            .applyIf(enabled) { clickable(onClick = onClick).pointerHoverIcon(PointerIcon.Hand) },
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled))
    ) {
        CompositionLocalProvider(LocalContentAlpha provides if (enabled) 1f else ContentAlpha.disabled) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}