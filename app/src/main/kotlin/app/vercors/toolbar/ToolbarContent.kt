package app.vercors.toolbar

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import app.vercors.LocalConfiguration
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.applyIf
import app.vercors.navigation.title
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@Composable
fun ToolbarContent(component: ToolbarComponent) {
    val uiState by component.uiState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.padding(UI.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
        ) {
            ToolbarButton(
                FeatherIcons.ChevronLeft,
                "Previous",
                interactionSource,
                uiState.hasPreviousScreen
            ) { component.onToolbarClick(ToolbarButton.Previous) }
            ToolbarButton(
                FeatherIcons.ChevronRight,
                "Next",
                interactionSource,
                uiState.hasNextScreen
            ) { component.onToolbarClick(ToolbarButton.Next) }
            ToolbarButton(
                FeatherIcons.RefreshCw,
                "Refresh",
                interactionSource,
                uiState.canRefreshScreen
            ) { component.onToolbarClick(ToolbarButton.Refresh) }
            uiState.title.forEachIndexed { index, config ->
                if (index != 0) Text(
                    text = " > ",
                    lineHeight = UI.normalLineHeight
                )
                Text(
                    text = config.title,
                    lineHeight = UI.normalLineHeight,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { component.onTitleClick(config) }
                    ).pointerHoverIcon(PointerIcon.Hand)
                )
            }
        }
        if (!LocalConfiguration.current.useSystemWindowFrame) {
            Spacer(Modifier.weight(1f))
            Row {
                WindowButton(FeatherIcons.Minus, "Minimize Window") { component.onToolbarClick(ToolbarButton.Minimize) }
                WindowButton(
                    FeatherIcons.Square,
                    "Maximize Window"
                ) { component.onToolbarClick(ToolbarButton.Maximize) }
                WindowButton(FeatherIcons.X, "Close Window", true) { component.onToolbarClick(ToolbarButton.Close) }
            }
        }
    }
}

@Composable
private fun ToolbarButton(
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
private fun WindowButton(
    icon: ImageVector,
    name: String,
    isRed: Boolean = false,
    onClick: () -> Unit
) {
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