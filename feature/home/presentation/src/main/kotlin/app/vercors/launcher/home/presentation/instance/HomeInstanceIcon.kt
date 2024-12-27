/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.launcher.home.presentation.instance

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.ui.AppAnimatedVisibility
import app.vercors.launcher.core.presentation.ui.darker
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.home.presentation.HomeSectionItemUi

@Composable
fun HomeInstanceIcon(
    instance: HomeSectionItemUi.Instance,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(modifier.aspectRatio(1f).padding(10.dp).hoverable(interactionSource)) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize(),
            //shape = UI.defaultRoundedCornerShape,
            //elevation = 1.dp
        ) {
            Icon(
                imageVector = appVectorResource { hard_drive },
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(10.dp)
            )
        }

        // TODO show different button based on instance status
        /*when (val status = instance.status) {
            InstanceStatus.NotRunning, InstanceStatus.Crashed, InstanceStatus.Killed -> InstanceIconButton(
                isHovered = isHovered,
                onClick = onLaunch,
                icon = FeatherIcons.Play,
                contentDescription = appStringResource { play }
            )

            is InstanceStatus.RefreshingToken -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = status.progress,
                    animationSpec = tween(500)
                )

                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(64.dp),
                    strokeWidth = 6.dp
                )
            }

            else -> InstanceIconButton(
                isHovered = isHovered,
                onClick = onStop,
                icon = FeatherIcons.X,
                color = MaterialTheme.colors.error,
                contentDescription = appStringResource { stop }
            )
        }*/
        HomeInstanceIconButton(
            isHovered = isHovered,
            onClick = onAction,
            icon = appVectorResource { play },
            contentDescription = appStringResource { play }
        )
    }
}

@Composable
private fun HomeInstanceIconButton(
    isHovered: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?
) {
    AppAnimatedVisibility(visible = isHovered) {
        Box(
            modifier = Modifier.fillMaxSize().background(darker),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onClick,
                colors = IconButtonDefaults.filledIconButtonColors(),
                modifier = Modifier.size(64.dp),
                content = {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }
    }
}
