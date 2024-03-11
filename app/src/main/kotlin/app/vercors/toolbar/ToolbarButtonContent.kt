package app.vercors.toolbar

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.applyIf
import app.vercors.common.AppTooltip

@Composable
fun ToolbarButtonContent(
    icon: ImageVector,
    name: String,
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TooltipArea(
        tooltip = { AppTooltip(name) }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            modifier = modifier.size(UI.mediumIconSize).applyIf(enabled) {
                pointerHoverIcon(PointerIcon.Hand).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
            },
            tint = if (enabled) LocalContentColor.current else LocalPalette.current.surface2
        )
    }
}