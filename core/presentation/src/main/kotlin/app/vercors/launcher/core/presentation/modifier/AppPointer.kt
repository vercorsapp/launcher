package app.vercors.launcher.core.presentation.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Stable
fun Modifier.handPointer() = pointerHoverIcon(PointerIcon.Hand)

@Composable
fun Modifier.clickableIcon(
    onClick: () -> Unit,
) = handPointer().clickableWithoutRipple(onClick = onClick)

@Composable
fun Modifier.clickableButton(
    onClick: () -> Unit,
) = handPointer().clickable(onClick = onClick)

