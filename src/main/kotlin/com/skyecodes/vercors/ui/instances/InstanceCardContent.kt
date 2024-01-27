package com.skyecodes.vercors.ui.instances

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.readable
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box

@Composable
fun InstanceCardContent(instance: Instance) {
    val locale = LocalLocalization.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding)
    ) {
        Box(Modifier.weight(1f).aspectRatio(1f)) {
            Surface(
                color = LocalPalette.current.surface2,
                modifier = Modifier.fillMaxSize(),
                shape = UI.defaultRoundedCornerShape,
                elevation = 1.dp
            ) {
                Icon(FeatherIcons.Box, null, Modifier.fillMaxSize())
            }
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = (instance.loader?.value ?: UI.vanilla) + " " + instance.gameVersion.id,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = instance.lastPlayed?.let { "${locale.lastPlayed} ${it.readable()}" }
                        ?: locale.notPlayedBefore,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}