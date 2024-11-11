package app.vercors.launcher.core.presentation.ui

import androidx.compose.runtime.Composable

@Composable
fun <T, R> T.runComposable(block: @Composable T.() -> R): R = block()