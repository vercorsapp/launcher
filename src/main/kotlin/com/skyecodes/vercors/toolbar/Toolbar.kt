package com.skyecodes.vercors.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.LocalConfiguration
import com.skyecodes.vercors.UI
import com.skyecodes.vercors.applyIf
import com.skyecodes.vercors.common.LocalPalette
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@Composable
fun Toolbar(
    screenTitle: String?,
    hasPreviousScreen: Boolean,
    hasNextScreen: Boolean,
    canRefreshScreen: Boolean,
    onNextScreen: () -> Unit,
    onPreviousScreen: () -> Unit,
    onRefreshScreen: () -> Unit,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.padding(UI.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
        ) {
            ToolbarButton(FeatherIcons.ChevronLeft, "Previous", interactionSource, hasPreviousScreen, onPreviousScreen)
            ToolbarButton(FeatherIcons.ChevronRight, "Next", interactionSource, hasNextScreen, onNextScreen)
            ToolbarButton(FeatherIcons.RefreshCw, "Refresh", interactionSource, canRefreshScreen, onRefreshScreen)
            screenTitle?.let {
                Text(
                    text = it,
                    lineHeight = UI.normalLineHeight
                )
            }
        }
        if (!LocalConfiguration.current.useSystemWindowFrame) {
            Spacer(Modifier.weight(1f))
            Row {
                WindowButton(FeatherIcons.Minus, "Minimize Window", onMinimize)
                WindowButton(FeatherIcons.Square, "Maximize Window", onMaximize)
                WindowButton(FeatherIcons.X, "Close Window", onClose, true)
            }
        }
    }
}

@Composable
fun ToolbarButton(
    icon: ImageVector,
    name: String,
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Icon(
        imageVector = icon,
        contentDescription = name,
        modifier = Modifier.size(UI.mediumIconSize).applyIf(enabled) {
            pointerHoverIcon(PointerIcon.Hand).clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
        },
        tint = if (enabled) LocalContentColor.current else LocalPalette.current.surface2
    )
}

@Composable
private fun WindowButton(icon: ImageVector, name: String, onClick: () -> Unit, isRed: Boolean = false) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        Modifier.fillMaxHeight().width(40.dp).pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick).hoverable(interactionSource)
            .background(if (isRed && isHovered) MaterialTheme.colors.error else MaterialTheme.colors.surface)
    ) {
        Icon(icon, name, Modifier.align(Alignment.Center).size(UI.mediumIconSize))
    }
}