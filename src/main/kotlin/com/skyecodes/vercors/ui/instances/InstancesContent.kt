package com.skyecodes.vercors.ui.instances

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.skyecodes.vercors.component.screen.InstancesComponent
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.IconTextButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Search


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InstancesContent(
    component: InstancesComponent
) {
    val instances by component.instances.collectAsState()
    val uiState by component.uiState.collectAsState()
    val locale = LocalLocalization.current

    instances?.let {
        Column(
            modifier = Modifier.padding(UI.largePadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(UI.largePadding)
        ) {
            Surface {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
                    verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                    modifier = Modifier.padding(UI.largePadding).fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.nameFilter,
                        onValueChange = component::updateNameFilter,
                        leadingIcon = { Icon(FeatherIcons.Search, null, Modifier.size(UI.mediumIconSize)) },
                        placeholder = { Text(locale.search) }
                    )
                    Row {
                        Text(locale.sortBy)
                    }
                }
            }
        }
    } ?: Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = locale.noInstancesFound,
            style = MaterialTheme.typography.h4
        )

        IconTextButton(
            onClick = component.openNewInstanceDialog,
            imageVector = FeatherIcons.Plus,
            text = locale.createNewInstance
        )
    }
}