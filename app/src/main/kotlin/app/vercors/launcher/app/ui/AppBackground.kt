package app.vercors.launcher.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.ui.runComposable

@Composable
fun ContentBackground(
    gradient: Boolean,
    content: @Composable () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val modifier: @Composable Modifier.() -> Modifier = if (gradient) {
        {
            background(
                largeRadialGradient(
                    background,
                    lerp(background, MaterialTheme.colorScheme.primaryContainer, 0.5f)
                )
            )
        }
    } else {
        { background(background) }
    }

    Box(modifier = Modifier.padding(bottom = 5.dp, end = 5.dp).fillMaxSize().runComposable(modifier)) {
        content()
    }
}

@Stable
private fun largeRadialGradient(background0: Color, background1: Color) = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        return RadialGradientShader(
            colors = listOf(background0, background0, background1),
            colorStops = listOf(0f, 0.3f, 1f),
            center = Offset(0f, 0f),
            radius = (size.height + size.width) / 1.5f,
        )
    }
}