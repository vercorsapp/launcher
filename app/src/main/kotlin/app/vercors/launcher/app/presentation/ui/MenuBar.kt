package app.vercors.launcher.app.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import app.vercors.launcher.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun WindowScope.MenuBar(
    searchQuery: String,
    isMaximized: Boolean,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
        WindowDraggableArea(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { onMaximize() })
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = vectorResource(Res.drawable.vercors),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(Res.string.app_title),
                    )
                    Text(
                        text = stringResource(Res.string.app_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Row(
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
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.padding(5.dp).width(600.dp).widthIn(min = 100.dp),
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
                }
                Row {
                    WindowButton(
                        imageVector = vectorResource(Res.drawable.minus),
                        contentDescription = null,
                        onClick = onMinimize
                    )
                    WindowButton(
                        imageVector = vectorResource(if (isMaximized) Res.drawable.minimize else Res.drawable.maximize),
                        contentDescription = null,
                        onClick = onMaximize
                    )
                    WindowButton(
                        imageVector = vectorResource(Res.drawable.x),
                        contentDescription = null,
                        onClick = onClose
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
) {
    Box(
        modifier = Modifier.clickable(onClick = onClick).fillMaxHeight().aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = contentDescription,
        )
    }
}