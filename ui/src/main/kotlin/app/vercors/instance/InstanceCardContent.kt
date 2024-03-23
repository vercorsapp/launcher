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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.common.AppAnimatedVisibility
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Eye
import compose.icons.feathericons.Play
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.play
import vercors.ui.generated.resources.view

@Composable
fun InstanceCardContent(
    instance: Instance,
    onInstanceClick: (Instance) -> Unit,
    onInstanceLaunchClick: (Instance) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Card(
        modifier = modifier.hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand).clickable { onInstanceClick(instance) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UI.largePadding),
            modifier = Modifier.padding(UI.largePadding)
        ) {
            Box(Modifier.fillMaxWidth().aspectRatio(1f)) {
                Surface(
                    color = LocalPalette.current.surface2,
                    modifier = Modifier.fillMaxSize(),
                    shape = UI.defaultRoundedCornerShape,
                    elevation = 1.dp
                ) {
                    Icon(FeatherIcons.Box, null, Modifier.fillMaxSize())
                }

                when (instance.status) {
                    InstanceStatus.Stopped -> {
                        AppAnimatedVisibility(
                            visible = isHovered,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(UI.darker),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = { onInstanceLaunchClick(instance) },
                                    modifier = Modifier.size(64.dp)
                                        .background(MaterialTheme.colors.primary, CircleShape),
                                    content = {
                                        Icon(
                                            imageVector = FeatherIcons.Play,
                                            contentDescription = stringResource(Res.string.play),
                                            modifier = Modifier.size(32.dp),
                                            tint = MaterialTheme.colors.onPrimary
                                        )
                                    }
                                )
                            }
                        }
                    }

                    is InstanceStatus.RefreshingToken -> {
                        Box(
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
                    }

                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { onInstanceClick(instance) },
                                modifier = Modifier.size(64.dp)
                                    .background(MaterialTheme.colors.primary, CircleShape),
                                content = {
                                    Icon(
                                        imageVector = FeatherIcons.Eye,
                                        contentDescription = stringResource(Res.string.view),
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }
                            )
                        }
                    }
                }


            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = instance.data.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = instance.loaderAndVersionString,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = instance.lastPlayedString,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}