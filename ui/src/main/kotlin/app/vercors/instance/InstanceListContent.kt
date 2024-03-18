/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import vercors.ui.generated.resources.*


@Composable
fun InstanceListContent(
    state: InstanceListState,
    onIntent: (InstanceListIntent) -> Unit
) {
    if (state.instanceList.isNotEmpty()) {
        InstanceListAreaContent(
            state = state,
            onIntent = onIntent,
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
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
                    onClick = { onIntent(InstanceListIntent.OpenCreateInstanceDialog) },
                    imageVector = FeatherIcons.Plus,
                    text = stringResource(Res.string.createInstance)
                )
            }
        }
    }
}

@Composable
private fun InstanceListAreaContent(
    state: InstanceListState,
    onIntent: (InstanceListIntent) -> Unit
) {
    val gridState = rememberLazyGridState()
    val scrollbarAdapter = rememberScrollbarAdapter(gridState)
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(state) {
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
                    value = state.nameFilter,
                    onValueChange = { onIntent(InstanceListIntent.UpdateNameFilter(it)) },
                    leadingIcon = { Icon(FeatherIcons.Search, null, Modifier.size(UI.mediumIconSize)) },
                    trailingIcon = {
                        if (state.nameFilter.isNotBlank()) Icon(
                            FeatherIcons.X, null,
                            Modifier.size(UI.mediumIconSize).pointerHoverIcon(PointerIcon.Default)
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    onIntent(InstanceListIntent.UpdateNameFilter(""))
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
                        value = state.sorter.sortBy,
                        textConverter = { it.title },
                        onValueChange = { onIntent(InstanceListIntent.UpdateSortBy(it)) },
                        showScrollbar = false,
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(it.title)
                    }
                    TooltipArea({ AppTooltip(state.sorter.sortByOrder.title) }) {
                        OutlinedFieldButton(
                            size = 32.dp,
                            onClick = { onIntent(InstanceListIntent.ToggleSortByOrder) },
                        ) {
                            Icon(
                                state.sorter.sortByOrder.icon,
                                state.sorter.sortByOrder.title,
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
                        value = state.sorter.groupBy,
                        textConverter = { it.title },
                        onValueChange = { onIntent(InstanceListIntent.UpdateGroupBy(it)) },
                        showScrollbar = false,
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(it.title)
                    }
                    TooltipArea({ AppTooltip(state.sorter.groupByOrder.title) }) {
                        OutlinedFieldButton(
                            size = 32.dp,
                            onClick = { onIntent(InstanceListIntent.ToggleGroupByOrder) },
                        ) {
                            Icon(
                                state.sorter.groupByOrder.icon,
                                state.sorter.groupByOrder.title,
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
                            enabled = state.canSaveFilters,
                            size = 32.dp,
                            onClick = { onIntent(InstanceListIntent.SaveFilters) },
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
                if (state.instanceGroups.isEmpty()) {
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
                } else if (state.sorter.groupBy === InstanceGroupBy.None) {
                    items(state.instanceGroups[""]!!, key = { it.id }) {
                        Card(modifier = Modifier.padding(UI.mediumPadding)) {
                            InstanceCardContent(
                                instance = it,
                                onInstanceClick = { onIntent(InstanceListIntent.ShowInstanceDetails(it)) },
                                onInstanceLaunchClick = { onIntent(InstanceListIntent.LaunchInstance(it)) }
                            )
                        }
                    }
                } else {
                    state.instanceGroups.forEach { (key, instances) ->
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
                        items(instances, key = { it.id }) {
                            Card(modifier = Modifier.padding(UI.mediumPadding)) {
                                InstanceCardContent(
                                    instance = it,
                                    onInstanceClick = { onIntent(InstanceListIntent.ShowInstanceDetails(it)) },
                                    onInstanceLaunchClick = { onIntent(InstanceListIntent.LaunchInstance(it)) }
                                )
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
