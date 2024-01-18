package com.skyecodes.vercors.ui.instances

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val instances = viewModel.instances.collectAsState()
    Column {
        Column {
            instances.value!!.forEach { Text(it.name) }
        }
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
                onClick = viewModel.openNewInstanceDialog,
                imageVector = FeatherIcons.Plus,
                text = UI.Text.CREATE_NEW_INSTANCE
            )
        }
    }
}