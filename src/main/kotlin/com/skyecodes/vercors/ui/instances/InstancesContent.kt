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
import com.skyecodes.vercors.logic.InstancesViewModel
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.IconTextButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus


@Composable
fun InstancesContent(
    viewModel: InstancesViewModel
) {
    var openDialog by remember { mutableStateOf(false) }
    val instances = viewModel.instances.collectAsState()

    if (instances.value!!.isEmpty()) {
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
                text = UI.Text.CREATE_NEW_INSTANCE
            )
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            instances.value!!.forEach { Text(it.name) }
        }
    }

    AnimatedVisibility(openDialog, enter = fadeIn(), exit = fadeOut()) {
        CreateInstanceDialogContent(
            onClose = { openDialog = false }
        )
    }
}