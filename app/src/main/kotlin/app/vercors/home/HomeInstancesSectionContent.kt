package app.vercors.home

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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.common.AppAnimatedVisibility
import app.vercors.instance.InstanceData
import app.vercors.instance.lastPlayedString
import app.vercors.instance.loaderAndVersionString
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Play
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.roundToInt

@Composable
fun HomeInstancesSectionContent(
    instances: ImmutableList<InstanceData>?,
    onInstanceClick: (InstanceData) -> Unit,
    onInstanceLaunchClick: (InstanceData) -> Unit
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.fillMaxWidth().onGloballyPositioned {
            count = localDensity.run { ((it.size.width.toDp() - 80.dp) / 200.dp).roundToInt() }
        }
    ) {
        if (instances?.isNotEmpty() == true) {
            for (instance in instances.take(count)) {
                InstanceCardContent(instance, onInstanceClick, onInstanceLaunchClick)
            }
            for (i in instances.size until count) {
                Spacer(Modifier.weight(1f))
            }
        } else {
            Text("No instances found :(")
        }
    }
}

@Composable
private fun RowScope.InstanceCardContent(
    instance: InstanceData,
    onInstanceClick: (InstanceData) -> Unit,
    onInstanceLaunchClick: (InstanceData) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Card(
        modifier = Modifier.weight(1f).hoverable(interactionSource)
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
                            modifier = Modifier.size(64.dp).background(MaterialTheme.colors.primary, CircleShape),
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
