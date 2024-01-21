package com.skyecodes.vercors.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.readable
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.IconTextButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Edit
import compose.icons.feathericons.Play
import kotlin.math.roundToInt

@Composable
fun HomeInstancesSectionContent(instances: List<Instance>?) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.fillMaxWidth().onGloballyPositioned {
            count = localDensity.run { ((it.size.width.toDp() - 80.dp) / 250.dp).roundToInt() }
        }
    ) {
        if (instances == null) {
            for (i in 0 until count) {
                FakeHomeCard { InstanceCardBox(it) }
            }
        } else if (instances.isNotEmpty()) {
            for (instance in instances.take(count)) {
                InstanceCardContent(instance)
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
private fun RowScope.InstanceCardBox(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit = {}
) {
    Card(modifier = modifier.weight(1f).aspectRatio(0.8f), shape = shape) {
        content()
    }
}

@Composable
private fun RowScope.InstanceCardContent(instance: Instance) {
    InstanceCardBox {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UI.largePadding),
            modifier = Modifier.padding(UI.largePadding)
        ) {
            Surface(
                color = LocalPalette.current.surface2,
                modifier = Modifier.weight(1f).aspectRatio(1f),
                shape = UI.defaultRoundedCornerShape,
                elevation = 1.dp
            ) {
                Icon(FeatherIcons.Box, null, Modifier.fillMaxSize())
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = instance.name,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    fontWeight = FontWeight.ExtraBold,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(UI.smallPadding, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (instance.loader?.value ?: "Vanilla") + " " + instance.gameVersion.id,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = instance.lastPlayed?.let { "${LocalLocalization.current.lastPlayed} ${it.readable()}" }
                            ?: LocalLocalization.current.notPlayedBefore,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconTextButton(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2),
                    imageVector = FeatherIcons.Edit
                )

                IconTextButton(
                    onClick = {},
                    imageVector = FeatherIcons.Play,
                    text = LocalLocalization.current.play
                )
            }
        }
    }
}