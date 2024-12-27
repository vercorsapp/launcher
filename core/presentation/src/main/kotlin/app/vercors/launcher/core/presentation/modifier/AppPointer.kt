package app.vercors.launcher.core.presentation.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Stable
fun Modifier.handPointer(enabled: Boolean = true) = if (enabled) pointerHoverIcon(PointerIcon.Hand) else this

@Composable
fun Modifier.clickableIcon(
    enabled: Boolean = true,
    onClick: () -> Unit,
) = handPointer(enabled).clickableWithoutRipple(onClick = onClick)

@Composable
fun Modifier.clickableButton(
    enabled: Boolean = true,
    onClick: () -> Unit,
) = handPointer(enabled).clickable(onClick = onClick)

