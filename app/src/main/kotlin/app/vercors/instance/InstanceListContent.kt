package app.vercors.instance

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.common.*
import app.vercors.header
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Save
import compose.icons.feathericons.Search
import compose.icons.feathericons.X
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*


@Composable
fun InstanceListContent(component: InstanceListComponent) {
    val uiState by component.uiState.collectAsState()

    if (uiState.instanceList.isNotEmpty()) {
        InstanceListAreaContent(component, uiState)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Text(
                    text = stringResource(Res.string.loading),
                    style = MaterialTheme.typography.h4
                )
            } else {
                Text(
                    text = stringResource(Res.string.noInstancesFound),
                    style = MaterialTheme.typography.h4
                )
                IconTextButton(
                    onClick = component::openCreateInstanceDialog,
                    imageVector = FeatherIcons.Plus,
                    text = stringResource(Res.string.createInstance)
                )
            }
        }
    }
}

@Composable
private fun InstanceListAreaContent(
    component: InstanceListComponent,
    uiState: InstanceListUiState
) {
    val gridState = rememberLazyGridState()
    val scrollbarAdapter = rememberScrollbarAdapter(gridState)
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(uiState) {
        gridState.scrollToItem(0)
    }

    Column {
        Card(Modifier.padding(top = UI.largePadding, start = UI.largePadding, end = UI.largePadding)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                modifier = Modifier.padding(UI.mediumPadding).fillMaxWidth().height(IntrinsicSize.Max)
            ) {
                OutlinedTextField(
                    value = uiState.nameFilter,
                    onValueChange = component::updateNameFilter,
                    leadingIcon = { Icon(FeatherIcons.Search, null, Modifier.size(UI.mediumIconSize)) },
                    trailingIcon = {
                        if (uiState.nameFilter.isNotBlank()) Icon(FeatherIcons.X, null,
                            Modifier.size(UI.mediumIconSize).pointerHoverIcon(PointerIcon.Default)
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    component.updateNameFilter("")
                                })
                    },
                    placeholder = { Text(stringResource(Res.string.search)) },
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.sortBy))
                    AppDropdownMenuBox(
                        options = InstanceSortBy.entries,
                        value = uiState.sorter.sortBy,
                        textConverter = { it.title },
                        onValueChange = component::updateSortBy,
                        showScrollbar = false,
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(it.title)
                    }
                    TooltipArea({ AppTooltip(uiState.sorter.sortByOrder.title) }) {
                        OutlinedFieldButton(
                            size = 32.dp,
                            onClick = component::toggleSortByOrder,
                        ) {
                            Icon(
                                uiState.sorter.sortByOrder.icon,
                                uiState.sorter.sortByOrder.title,
                                Modifier.size(UI.mediumIconSize)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.groupBy))
                    AppDropdownMenuBox(
                        options = InstanceGroupBy.entries,
                        value = uiState.sorter.groupBy,
                        textConverter = { it.title },
                        onValueChange = component::updateGroupBy,
                        showScrollbar = false,
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(it.title)
                    }
                    TooltipArea({ AppTooltip(uiState.sorter.groupByOrder.title) }) {
                        OutlinedFieldButton(
                            size = 32.dp,
                            onClick = component::toggleGroupByOrder,
                        ) {
                            Icon(
                                uiState.sorter.groupByOrder.icon,
                                uiState.sorter.groupByOrder.title,
                                Modifier.size(UI.mediumIconSize)
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    TooltipArea(
                        tooltip = { AppTooltip(stringResource(Res.string.saveFilters)) }
                    ) {
                        OutlinedFieldButton(
                            enabled = uiState.canSaveFilters,
                            size = 32.dp,
                            onClick = component::saveFilters,
                        ) {
                            Icon(
                                FeatherIcons.Save,
                                stringResource(Res.string.saveFilters),
                                Modifier.size(UI.mediumIconSize)
                            )
                        }
                    }
                }
            }
        }

        Box(Modifier.padding(start = UI.mediumPadding)) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 200.dp),
                modifier = Modifier.padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.largePadding)
            ) {
                if (uiState.instanceGroups.isEmpty()) {
                    item {
                        Text(
                            text = "No instances found!",
                            modifier = Modifier.padding(
                                start = UI.mediumPadding,
                                end = UI.mediumPadding,
                                top = UI.smallPadding
                            )
                        )
                    }
                } else if (uiState.sorter.groupBy === InstanceGroupBy.None) {
                    items(uiState.instanceGroups[""]!!, key = { it.path }) {
                        Card(modifier = Modifier.padding(UI.mediumPadding)) {
                            InstanceCardContent(it, component::showInstanceDetails, component::onLaunchInstance)
                        }
                    }
                } else {
                    uiState.instanceGroups.forEach { (key, instances) ->
                        header(key) {
                            Row(
                                modifier = Modifier.padding(
                                    start = UI.mediumPadding,
                                    end = UI.mediumPadding,
                                    top = UI.smallPadding
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.h6
                                )
                                Divider(
                                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                                    thickness = 1.dp,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                        }
                        items(instances, key = { it.path }) {
                            Card(modifier = Modifier.padding(UI.mediumPadding)) {
                                InstanceCardContent(it, component::showInstanceDetails, component::onLaunchInstance)
                            }
                        }
                    }
                }
            }
            VerticalScrollbar( // TODO fix scrollbar
                modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
                adapter = scrollbarAdapter
            )
        }
    }
}
