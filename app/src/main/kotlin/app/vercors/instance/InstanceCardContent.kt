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
import compose.icons.feathericons.Play

@Composable
fun InstanceCardContent(
    instance: InstanceData,
    onInstanceClick: (InstanceData) -> Unit,
    onInstanceLaunchClick: (InstanceData) -> Unit,
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
                    is InstanceStatus.Errored -> {

                    }

                    is InstanceStatus.Launching -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            val animatedProgress by animateFloatAsState(
                                targetValue = (instance.status as InstanceStatus.Launching).progress,
                                animationSpec = tween(500)
                            )

                            CircularProgressIndicator(
                                progress = animatedProgress,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    InstanceStatus.Running -> {

                    }

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
                                            FeatherIcons.Play,
                                            "Play",
                                            Modifier.size(32.dp),
                                            MaterialTheme.colors.onPrimary
                                        )
                                    }
                                )
                            }
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
                    text = instance.name,
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