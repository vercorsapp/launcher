package com.skyecodes.snowball.ui.instances

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skyecodes.snowball.ui.Data
import com.skyecodes.snowball.ui.UI
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Server

object InstancesTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Instances"
            val icon = rememberVectorPainter(FontAwesomeIcons.Solid.Server)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val instances = Data.instances.current

        if (instances.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = UI.Text.NO_INSTANCES_FOUND,
                    style = MaterialTheme.typography.h4
                )

                OutlinedButton(
                    onClick = {},
                    border = BorderStroke(ButtonDefaults.OutlinedBorderSize, UI.colors.green),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = UI.colors.green
                    )
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Plus,
                        contentDescription = "Plus",
                        modifier = Modifier.size(UI.mediumIconSize)
                    )
                    Text(
                        text = UI.Text.CREATE_NEW_INSTANCE,
                        fontWeight = FontWeight.Bold,
                        fontSize = UI.subtitleFontSize,
                        modifier = Modifier.padding(start = UI.mediumPadding)
                    )
                }
            }
        }
    }
}