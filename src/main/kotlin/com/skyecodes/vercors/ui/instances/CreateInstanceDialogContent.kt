package com.skyecodes.vercors.ui.instances

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ChipDefaults.filterChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.applyIf
import com.skyecodes.vercors.component.dialog.CreateNewInstanceComponent
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.resourceAsStream
import com.skyecodes.vercors.ui.LocalConfiguration
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.IconTextButton
import com.skyecodes.vercors.ui.common.ScrollableExposedDropdownMenu
import com.skyecodes.vercors.ui.common.loadSvgPainter
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateInstanceDialogContent(component: CreateNewInstanceComponent) {
    val uiState by component.uiState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    /*LaunchedEffect(true) {
        val manifest = mojangService.getVersionManifest()
        versionManifest = manifest
        minecraftVersion = manifest.versions[manifest.latest.release]
    }

    LaunchedEffect(minecraftVersion) {
        minecraftVersionDisplay = minecraftVersion?.let { getVersionText(versionManifest!!, it) } ?: ""
    }

    LaunchedEffect(includeSnapshots) {
        if (!includeSnapshots && minecraftVersion?.type == MojangReleaseType.Snapshot) minecraftVersion =
            versionManifest?.latestRelease
    }*/

    Column(
        modifier = Modifier.padding(UI.largePadding)
            .applyIf(LocalConfiguration.current.animations) { animateContentSize() },
        verticalArrangement = Arrangement.spacedBy(UI.largePadding)
    ) {
        Text(
            text = UI.Text.CREATE_NEW_INSTANCE,
            style = MaterialTheme.typography.h6
        )

        FormField(UI.Text.INSTANCE_NAME) {
            OutlinedTextField(
                value = uiState.instanceName,
                onValueChange = component::updateInstanceName,
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormField(UI.Text.MINECRAFT_VERSION) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = uiState.isMinecraftVersionDropdownExpanded(),
                    onExpandedChange = component::updateMinecraftVersionDropdown,
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = uiState.minecraftVersion,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Default, overrideDescendants = true)
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = if (uiState.isMinecraftVersionDropdownExpanded()) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                contentDescription = "Show options"
                            )
                        }
                    )

                    ScrollableExposedDropdownMenu(
                        expanded = uiState.isMinecraftVersionDropdownExpanded(),
                        onDismissRequest = { component.updateMinecraftVersionDropdown(false) }
                    ) {
                        uiState.minecraftVersions
                            .filter { version -> version.isRelease || uiState.includeSnapshots }
                            .forEach { version ->
                                DropdownMenuItem(
                                    onClick = {
                                        component.updateMinecraftVersionDropdown(false)
                                        component.updateMinecraftVersion(version)
                                    },
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(version.text)
                                        version.icon?.let {
                                            Icon(it, null, Modifier.size(UI.mediumIconSize))
                                        }
                                    }
                                }
                            }
                    }
                }

                Checkbox(
                    modifier = Modifier.padding(start = UI.smallPadding),
                    checked = uiState.includeSnapshots,
                    onCheckedChange = component::updateIncludeSnapshots
                )
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = component::toggleIncludeSnapshots
                    ),
                    text = UI.Text.INCLUDE_SNAPSHOTS,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }

        FormField(UI.Text.LOADER) {
            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                LoaderChip(null, uiState.loader == null) { component.updateLoader(null) }
                Loader.entries.forEach {
                    LoaderChip(it, uiState.loader == it) { component.updateLoader(it) }
                }
            }
        }

        if (uiState.loader != null) {
            FormField(UI.Text.LOADER_VERSION) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = uiState.isLoaderVersionDropdownExpanded(),
                        onExpandedChange = component::updateLoaderVersionDropdown,
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = uiState.loaderVersion,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.pointerHoverIcon(
                                PointerIcon.Default,
                                overrideDescendants = true
                            ).fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (uiState.isLoaderVersionDropdownExpanded()) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                    contentDescription = "Show options"
                                )
                            }
                        )

                        ScrollableExposedDropdownMenu(
                            expanded = uiState.isLoaderVersionDropdownExpanded(),
                            onDismissRequest = { component.updateLoaderVersionDropdown(false) }
                        ) {

                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = component::close
            ) {
                Icon(
                    imageVector = FeatherIcons.X,
                    contentDescription = null,
                    modifier = Modifier.size(UI.mediumIconSize)
                )
                Text(
                    text = UI.Text.CANCEL,
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }

            IconTextButton(
                enabled = uiState.isValid,
                onClick = component::createInstance,
                imageVector = FeatherIcons.Plus,
                text = UI.Text.CREATE
            )
        }
    }
}

@Composable
private fun FormField(name: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
        Text(name, style = MaterialTheme.typography.subtitle2)
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LoaderChip(value: Loader?, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        trailingIcon = {
            value?.let {
                Icon(
                    loadSvgPainter(resourceAsStream("/icon/svg/${value.value}.svg"), LocalDensity.current),
                    value.name, Modifier.size(UI.mediumIconSize)
                )
            } ?: Icon(FeatherIcons.Feather, "Vanilla", Modifier.size(UI.mediumIconSize))
        },
        colors = filterChipColors(
            backgroundColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        )
    ) {
        Text(value?.name ?: "Vanilla", style = MaterialTheme.typography.body1)
    }
}
