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
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.ui.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun WindowScope.AppMenuBar(
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
            imageVector = vectorResource(CoreDrawable.vercors),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(CoreString.app_title),
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp)
                    .applyIf(canGoBack) { clickableIcon { onAction(MenuBarAction.Back) } },
                imageVector = vectorResource(CoreDrawable.chevron_left),
                tint = backButtonColor,
                contentDescription = stringResource(CoreString.back),
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
            imageVector = vectorResource(CoreDrawable.minus),
            contentDescription = null,
            onClick = { onAction(MenuBarAction.Minimize) }
        )
        WindowButton(
            imageVector = vectorResource(if (isMaximized) CoreDrawable.minimize else CoreDrawable.maximize),
            contentDescription = null,
            onClick = { onAction(MenuBarAction.Maximize) }
        )
        WindowButton(
            imageVector = vectorResource(CoreDrawable.x),
            contentDescription = null,
            onClick = { onAction(MenuBarAction.Close) },
            isClose = true
        )
    }
}


@Composable
private fun WindowButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    isClose: Boolean = false
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val isHovered by mutableInteractionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier.clickableButton(onClick = onClick).fillMaxHeight().aspectRatio(1f)
            .applyIf(isClose) { hoverable(mutableInteractionSource) }
            .applyIf(isHovered) { background(MaterialTheme.colorScheme.error) },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = imageVector,
            tint = if (isClose && isHovered) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
            contentDescription = contentDescription,
        )
    }
}