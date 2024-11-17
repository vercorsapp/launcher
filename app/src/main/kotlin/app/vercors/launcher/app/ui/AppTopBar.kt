package app.vercors.launcher.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import app.vercors.launcher.core.presentation.modifier.clickableButton
import app.vercors.launcher.core.presentation.modifier.clickableIcon
import app.vercors.launcher.core.presentation.ui.AppAnimatedContent
import app.vercors.launcher.core.presentation.ui.appAnimateColorAsState
import app.vercors.launcher.core.presentation.ui.thenIf
import app.vercors.launcher.core.resources.*

@Composable
fun WindowScope.AppTopBar(
    screenName: String,
    hasWindowControls: Boolean,
    canGoBack: Boolean,
    isMaximized: Boolean,
    onAction: (MenuBarAction) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
        WindowDraggableArea(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { onAction(MenuBarAction.Maximize) })
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                AppWindowTitle(
                    screenName = screenName,
                    canGoBack = canGoBack,
                    onAction = onAction
                )
                if (hasWindowControls) {
                    AppWindowButtons(
                        isMaximized = isMaximized,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.AppWindowTitle(
    screenName: String,
    canGoBack: Boolean,
    onAction: (MenuBarAction) -> Unit
) {
    val backButtonColor by appAnimateColorAsState(
        if (canGoBack) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.surfaceBright
    )

    Row(
        modifier = Modifier.padding(20.dp).weight(1f),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(width = 40.dp, height = 32.dp),
            imageVector = appVectorResource { vercors },
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = appStringResource { app_title },
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp).thenIf(canGoBack) { clickableIcon { onAction(MenuBarAction.Back) } },
                imageVector = appVectorResource { chevron_left },
                tint = backButtonColor,
                contentDescription = appStringResource { back },
            )
            AppAnimatedContent(targetState = screenName) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AppWindowButtons(
    isMaximized: Boolean,
    onAction: (MenuBarAction) -> Unit
) {
    Row {
        WindowButton(
            imageVector = appVectorResource { minus },
            contentDescription = null,
            onClick = { onAction(MenuBarAction.Minimize) }
        )
        WindowButton(
            imageVector = appVectorResource { maximize },
            contentDescription = null,
            onClick = { onAction(MenuBarAction.Maximize) }
        )
        WindowButton(
            imageVector = appVectorResource { x },
            contentDescription = null,
            onClick = { onAction(MenuBarAction.Close) },
            isCloseButton = true
        )
    }
}


@Composable
private fun WindowButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    isCloseButton: Boolean = false
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val isCloseButtonHovered by mutableInteractionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier.clickableButton(onClick = onClick).fillMaxHeight().aspectRatio(1f)
            .thenIf(isCloseButton) { hoverable(mutableInteractionSource) }
            .thenIf(isCloseButtonHovered) { background(MaterialTheme.colorScheme.error) },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = imageVector,
            tint = if (isCloseButtonHovered) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
            contentDescription = contentDescription,
        )
    }
}