package com.skyecodes.vercors.ui.instances

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.component.screen.InstancesComponent
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.app.InstanceGroupBy
import com.skyecodes.vercors.data.model.app.InstanceSortBy
import com.skyecodes.vercors.header
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.*


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun InstancesContent(
    component: InstancesComponent
) {
    val instances by component.instances.collectAsState()
    val uiState by component.uiState.collectAsState()
    val locale = LocalLocalization.current
    val gridState = rememberLazyGridState()
    val scrollbarAdapter = rememberScrollbarAdapter(gridState)
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(uiState) {
        gridState.scrollToItem(0)
    }

    if (instances.isNotEmpty()) {
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
                        placeholder = { Text(locale.search) },
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(locale.sortBy)
                        AppDropdownMenuBox(
                            options = InstanceSortBy.entries,
                            value = uiState.sorter.sortBy,
                            textConverter = { it.localizedName(locale) },
                            onValueChange = component::updateSortBy,
                            showScrollbar = false,
                            modifier = Modifier.width(250.dp)
                        ) {
                            Text(it.localizedName(locale))
                        }
                        TooltipArea({ AppTooltip(uiState.sorter.sortByOrder.localizedName(locale)) }) {
                            OutlinedFieldButton(
                                size = 32.dp,
                                onClick = component::toggleSortByOrder,
                            ) {
                                Icon(
                                    uiState.sorter.sortByOrder.icon,
                                    uiState.sorter.sortByOrder.localizedName(locale),
                                    Modifier.size(UI.mediumIconSize)
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(locale.groupBy)
                        AppDropdownMenuBox(
                            options = InstanceGroupBy.entries,
                            value = uiState.sorter.groupBy,
                            textConverter = { it.localizedName(locale) },
                            onValueChange = component::updateGroupBy,
                            showScrollbar = false,
                            modifier = Modifier.width(250.dp)
                        ) {
                            Text(it.localizedName(locale))
                        }
                        TooltipArea({ AppTooltip(uiState.sorter.groupByOrder.localizedName(locale)) }) {
                            OutlinedFieldButton(
                                size = 32.dp,
                                onClick = component::toggleGroupByOrder,
                            ) {
                                Icon(
                                    uiState.sorter.groupByOrder.icon,
                                    uiState.sorter.groupByOrder.localizedName(locale),
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
                            tooltip = { AppTooltip(locale.saveFilters) }
                        ) {
                            OutlinedFieldButton(
                                enabled = uiState.canSaveFilters,
                                size = 32.dp,
                                onClick = component::saveFilters,
                            ) {
                                Icon(
                                    FeatherIcons.Save,
                                    locale.saveFilters,
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
                    if (uiState.instances.isEmpty()) {
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
                        items(uiState.instances[""]!!, key = { it.path }) {
                            Card(modifier = Modifier.padding(UI.mediumPadding)) {
                                InstanceCardContent(it, component.showInstanceDetails, component.launchInstance)
                            }
                        }
                    } else {
                        uiState.instances.forEach { (key, instances) ->
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
                                    InstanceCardContent(it, component.showInstanceDetails, component.launchInstance)
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
    } else {
        Column(
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
}

@Composable
fun InstanceCardContent(
    instance: Instance,
    onInstanceClick: (Instance) -> Unit,
    onInstanceLaunchClick: (Instance) -> Unit
) {
    val locale = LocalLocalization.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Card(
        modifier = Modifier.hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand).clickable { onInstanceClick(instance) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UI.largePadding),
            modifier = Modifier.padding(UI.largePadding)
        ) {
            Box(Modifier.fillMaxWidth().aspectRatio(1f)) {
                Surface(
                    color = LocalPalette.current.surface2,
                    modifier = Modifier.fillMaxSize(),
                    shape = UI.defaultRoundedCornerShape,
                    elevation = 1.dp
                ) {
                    Icon(FeatherIcons.Box, null, Modifier.fillMaxSize())
                }
                AppAnimatedVisibility(
                    visible = isHovered,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(UI.darker),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onInstanceLaunchClick(instance) },
                            modifier = Modifier.size(64.dp).background(MaterialTheme.colors.primary, CircleShape),
                            content = {
                                Icon(
                                    FeatherIcons.Play,
                                    "Play",
                                    Modifier.size(32.dp),
                                    MaterialTheme.colors.onPrimary
                                )
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = instance.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = instance.loaderAndVersionString,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = instance.lastPlayedString(locale),
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}