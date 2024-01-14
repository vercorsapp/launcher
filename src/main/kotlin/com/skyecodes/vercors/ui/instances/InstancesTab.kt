package com.skyecodes.vercors.ui.instances

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skyecodes.vercors.ui.Data
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.util.IconTextButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Plus

object InstancesTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Instances"
            val icon = rememberVectorPainter(FeatherIcons.Box)

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
        var openDialog by remember { mutableStateOf(false) }
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

                IconTextButton(
                    onClick = { openDialog = true },
                    imageVector = FeatherIcons.Plus,
                    text = UI.Text.CREATE_NEW_INSTANCE,
                    colors = UI.successButtonColors
                )
            }

            AnimatedVisibility(openDialog, enter = fadeIn(), exit = fadeOut()) {
                CreateInstanceDialog(
                    onClose = { openDialog = false }
                )
            }
        } else {
            Column {
                instances.forEach { Text(it.name) }
            }
        }
    }
}