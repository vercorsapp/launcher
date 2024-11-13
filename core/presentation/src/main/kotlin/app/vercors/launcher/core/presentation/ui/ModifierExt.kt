package app.vercors.launcher.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.thenIf(condition: Boolean, modifier: @Composable Modifier.() -> Modifier): Modifier =
    if (condition) modifier() else this