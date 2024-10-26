package app.vercors.launcher.app.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import app.vercors.launcher.app.presentation.action.MenuBarAction
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.ui.applyIf
import app.vercors.launcher.core.presentation.ui.defaultAnimation
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun WindowScope.MenuBar(
    screenName: String,
    searchQuery: String,
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
                Row(
                    modifier = Modifier.padding(20.dp).weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(width = 44.dp, height = 32.dp),
                        imageVector = vectorResource(Res.drawable.vercors),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(Res.string.app_title),
                    )
                    AnimatedContent(
                        targetState = screenName,
                        transitionSpec = { defaultAnimation }
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                /*Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Spacer(Modifier.width(50.dp))
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { onAction(MenuBarAction.SearchQueryChange(it)) },
                            modifier = Modifier.padding(5.dp).width(600.dp).widthIn(min = 150.dp),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = stringResource(Res.string.search_bar),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        )
                    }
                    Spacer(Modifier.width(50.dp))
                }*/
                Row {
                    WindowButton(
                        imageVector = vectorResource(Res.drawable.minus),
                        contentDescription = null,
                        onClick = { onAction(MenuBarAction.Minimize) }
                    )
                    WindowButton(
                        imageVector = vectorResource(if (isMaximized) Res.drawable.minimize else Res.drawable.maximize),
                        contentDescription = null,
                        onClick = { onAction(MenuBarAction.Maximize) }
                    )
                    WindowButton(
                        imageVector = vectorResource(Res.drawable.x),
                        contentDescription = null,
                        onClick = { onAction(MenuBarAction.Close) },
                        isClose = true
                    )
                }
            }
        }
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
        modifier = Modifier.clickable(onClick = onClick).fillMaxHeight().aspectRatio(1f)
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