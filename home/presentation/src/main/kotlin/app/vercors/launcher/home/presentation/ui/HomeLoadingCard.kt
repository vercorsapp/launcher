package app.vercors.launcher.home.presentation.ui

import androidx.compose.animation.core.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush

@Composable
fun HomeLoadingCard(box: @Composable (Modifier) -> Unit) {
    val colors = listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)
    val limit = 1.5f
    val transition = rememberInfiniteTransition()
    val progressAnimated by transition.animateFloat(
        initialValue = -limit,
        targetValue = limit,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    box(Modifier.drawWithCache {
        val width = size.width
        val height = size.height

        val offset = width * progressAnimated

        val brush = Brush.linearGradient(
            colors = colors,
            start = Offset(offset, 0f),
            end = Offset(offset + width, height)
        )

        onDrawWithContent {
            drawRoundRect(
                brush = brush,
                blendMode = BlendMode.SrcIn,
                cornerRadius = CornerRadius(5f)
            )
        }
    })
}