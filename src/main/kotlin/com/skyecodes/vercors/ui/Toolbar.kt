package com.skyecodes.vercors.ui

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
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.applyIf
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@Composable
fun Toolbar(
    screenTitle: String,
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
            Icon(
                FeatherIcons.ChevronLeft,
                "Previous",
                modifier = Modifier.size(UI.mediumIconSize).applyIf(hasPreviousScreen) {
                    clickable(interactionSource = interactionSource, indication = null) { onPreviousScreen() }
                },
                tint = if (hasPreviousScreen) LocalContentColor.current else LocalPalette.current.surface2
            )
            Icon(
                FeatherIcons.ChevronRight,
                "Next",
                modifier = Modifier.size(UI.mediumIconSize).applyIf(hasNextScreen) {
                    clickable(interactionSource = interactionSource, indication = null) { onNextScreen() }
                },
                tint = if (hasNextScreen) LocalContentColor.current else LocalPalette.current.surface2
            )
            Icon(
                FeatherIcons.RefreshCw,
                "Refresh",
                modifier = Modifier.size(UI.mediumIconSize).applyIf(canRefreshScreen) {
                    clickable(interactionSource = interactionSource, indication = null) { onRefreshScreen() }
                },
                tint = if (canRefreshScreen) LocalContentColor.current else LocalPalette.current.surface2
            )
            Text(screenTitle)
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
private fun WindowButton(icon: ImageVector, name: String, onClick: () -> Unit, isRed: Boolean = false) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        Modifier.fillMaxHeight().width(40.dp).clickable(onClick = onClick).hoverable(interactionSource)
            .background(if (isRed && isHovered) MaterialTheme.colors.error else MaterialTheme.colors.surface)
    ) {
        Icon(icon, name, Modifier.align(Alignment.Center).size(UI.mediumIconSize))
    }
}