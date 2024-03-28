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

package app.vercors.instance

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.common.AppAnimatedVisibility
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Play
import compose.icons.feathericons.X
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.play
import vercors.ui.generated.resources.stop

@Composable
fun InstanceIconContent(instance: Instance, onLaunch: () -> Unit, onStop: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(modifier.aspectRatio(1f).hoverable(interactionSource)) {
        Surface(
            color = LocalPalette.current.surface2,
            modifier = Modifier.fillMaxSize(),
            shape = UI.defaultRoundedCornerShape,
            elevation = 1.dp
        ) {
            Icon(FeatherIcons.Box, null, Modifier.fillMaxSize())
        }

        when (instance.status) {
            InstanceStatus.NotRunning, InstanceStatus.Crashed, InstanceStatus.Killed -> InstanceIconButton(
                isHovered = isHovered,
                onClick = onLaunch,
                icon = FeatherIcons.Play,
                contentDescription = stringResource(Res.string.play)
            )

            is InstanceStatus.RefreshingToken -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = (instance.status as InstanceStatus.RefreshingToken).progress,
                    animationSpec = tween(500)
                )

                CircularProgressIndicator(
                    progress = animatedProgress,
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(64.dp)
                )
            }

            else -> InstanceIconButton(
                isHovered = isHovered,
                onClick = onStop,
                icon = FeatherIcons.X,
                color = MaterialTheme.colors.error,
                contentDescription = stringResource(Res.string.stop)
            )
        }
    }
}

@Composable
private fun InstanceIconButton(
    isHovered: Boolean,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(color),
    icon: ImageVector,
    contentDescription: String?
) = AppAnimatedVisibility(
    visible = isHovered,
    enter = fadeIn(),
    exit = fadeOut()
) {
    Box(
        modifier = Modifier.fillMaxSize().background(UI.darker),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(64.dp).pointerHoverIcon(PointerIcon.Hand)
                .background(color, CircleShape),
            content = {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(32.dp),
                    tint = contentColor
                )
            }
        )
    }
}
