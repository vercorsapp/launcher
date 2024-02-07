package com.skyecodes.vercors.ui.instances

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.component.screen.InstanceDetailsComponent
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.IconTextButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Settings

@Composable
fun InstanceDetailsContent(component: InstanceDetailsComponent) {
    val locale = LocalLocalization.current

    Column {
        Card(Modifier.padding(UI.largePadding)) {
            Column {
                Row(
                    modifier = Modifier.height(150.dp).fillMaxWidth().padding(UI.largePadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(UI.largePadding)
                ) {
                    Surface(
                        color = LocalPalette.current.surface2,
                        modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                        shape = UI.defaultRoundedCornerShape,
                        elevation = 1.dp
                    ) {
                        Icon(FeatherIcons.Box, null, Modifier.fillMaxSize())
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(UI.smallPadding, Alignment.CenterVertically)
                    ) {
                        Text(component.instance.name, style = MaterialTheme.typography.h5)
                        Column {
                            Text(component.instance.loaderAndVersionString)
                            Text(component.instance.lastPlayedString(locale))
                        }
                    }
                }
                Row {
                    IconTextButton(
                        onClick = {},
                        imageVector = FeatherIcons.Settings,
                        text = locale.settings
                    )
                }
            }
        }
    }
}