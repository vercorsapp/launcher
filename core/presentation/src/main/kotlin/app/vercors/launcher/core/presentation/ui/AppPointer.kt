package app.vercors.launcher.core.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Stable
fun Modifier.handPointer() = pointerHoverIcon(PointerIcon.Hand)

@Composable
fun Modifier.clickableIcon(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
) = pointerHoverIcon(PointerIcon.Hand).clickable(
    interactionSource = interactionSource,
    indication = null,
    onClick = onClick
)

@Composable
fun Modifier.clickableButton(
    onClick: () -> Unit,
) = pointerHoverIcon(PointerIcon.Hand).clickable(onClick = onClick)

