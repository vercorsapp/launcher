package com.skyecodes.vercors.ui.instances

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ChipDefaults.filterChipColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.data.model.mojang.MojangReleaseType
import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import com.skyecodes.vercors.data.model.mojang.get
import com.skyecodes.vercors.data.service.InstanceService
import com.skyecodes.vercors.data.service.MojangService
import com.skyecodes.vercors.resourceAsStream
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.IconTextButton
import com.skyecodes.vercors.ui.common.ScrollableExposedDropdownMenu
import com.skyecodes.vercors.ui.common.loadSvgPainter
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateInstanceDialogContent(onClose: () -> Unit) {
    val mojangService = koinInject<MojangService>()
    val instanceService = koinInject<InstanceService>()
    val interactionSource = remember { MutableInteractionSource() }
    var versionManifest: MojangVersionManifest? by remember { mutableStateOf(null) }
    var instanceName by remember { mutableStateOf("") }
    var isMinecraftVersionDropdownMenuExpanded by remember { mutableStateOf(false) }
    var minecraftVersion by remember { mutableStateOf<MojangVersionManifest.Version?>(null) }
    var minecraftVersionDisplay by remember { mutableStateOf("") }
    var includeSnapshots by remember { mutableStateOf(false) }
    var loader by remember { mutableStateOf<Loader?>(null) }
    var isLoaderVersionDropdownMenuExpanded by remember { mutableStateOf(false) }
    var loaderVersion by remember { mutableStateOf<String?>(null) }
    var loaderVersionDisplay by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
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
    }

    Dialog(
        onDismissRequest = onClose
    ) {
        Card {
            Column(
                modifier = Modifier.padding(UI.largePadding),
                verticalArrangement = Arrangement.spacedBy(UI.largePadding)
            ) {
                Text(
                    text = UI.Text.CREATE_NEW_INSTANCE,
                    style = MaterialTheme.typography.h6
                )

                FormField(UI.Text.INSTANCE_NAME) {
                    OutlinedTextField(
                        value = instanceName,
                        onValueChange = { instanceName = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                FormField(UI.Text.MINECRAFT_VERSION) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isMinecraftVersionDropdownMenuExpanded,
                            onExpandedChange = { isMinecraftVersionDropdownMenuExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = minecraftVersionDisplay,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Default, overrideDescendants = true)
                                    .fillMaxWidth(),
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (isMinecraftVersionDropdownMenuExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                        contentDescription = "Show options"
                                    )
                                }
                            )

                            ScrollableExposedDropdownMenu(
                                expanded = isMinecraftVersionDropdownMenuExpanded,
                                onDismissRequest = { isMinecraftVersionDropdownMenuExpanded = false }
                            ) {
                                versionManifest?.let { manifest ->
                                    manifest.versions
                                        .filter { version -> version.type == MojangReleaseType.Release || includeSnapshots }
                                        .forEach { version ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    isMinecraftVersionDropdownMenuExpanded = false
                                                    minecraftVersion = version
                                                },
                                                contentPadding = PaddingValues(horizontal = 10.dp)
                                            ) {
                                                Row(
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(getVersionText(manifest, version))
                                                    getVersionIcon(manifest, version)?.let {
                                                        Icon(
                                                            it,
                                                            null,
                                                            Modifier.size(UI.mediumIconSize)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                        }

                        Checkbox(
                            modifier = Modifier.padding(start = UI.smallPadding),
                            checked = includeSnapshots,
                            onCheckedChange = { includeSnapshots = it },
                        )
                        Text(
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { includeSnapshots = !includeSnapshots }
                            ),
                            text = UI.Text.INCLUDE_SNAPSHOTS,
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }

                FormField(UI.Text.LOADER) {
                    Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                        LoaderChip(null, loader == null) { loader = null }
                        Loader.entries.forEach {
                            LoaderChip(it, loader == it) { loader = it }
                        }
                    }
                }

                if (loader != null) {
                    FormField(UI.Text.LOADER_VERSION) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = isLoaderVersionDropdownMenuExpanded,
                                onExpandedChange = { isLoaderVersionDropdownMenuExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = loaderVersionDisplay,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.pointerHoverIcon(
                                        PointerIcon.Default,
                                        overrideDescendants = true
                                    ).fillMaxWidth(),
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (isLoaderVersionDropdownMenuExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                            contentDescription = "Show options"
                                        )
                                    }
                                )

                                ScrollableExposedDropdownMenu(
                                    expanded = isLoaderVersionDropdownMenuExpanded,
                                    onDismissRequest = { isLoaderVersionDropdownMenuExpanded = false }
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
                        onClick = onClose
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
                        enabled = instanceName.isNotBlank() && minecraftVersion != null && (loader == null || !loaderVersion.isNullOrBlank()),
                        onClick = {
                            scope.launch {
                                instanceService.createInstance(
                                    instanceName,
                                    minecraftVersion!!,
                                    loader,
                                    loaderVersion
                                )
                            }
                            onClose()
                        },
                        imageVector = FeatherIcons.Plus,
                        text = UI.Text.CREATE,
                        colors = UI.successButtonColors
                    )
                }
            }
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

@Stable
private fun getVersionText(manifest: MojangVersionManifest, version: MojangVersionManifest.Version) =
    if (manifest.isLatestRelease(version)) "Latest release (${version.id})"
    else if (manifest.isLatestSnapshot(version)) "Latest snapshot (${version.id})"
    else version.id

@Stable
private fun getVersionIcon(manifest: MojangVersionManifest, version: MojangVersionManifest.Version) =
    if (manifest.isLatestRelease(version)) FeatherIcons.Star
    else if (manifest.isLatestSnapshot(version)) FeatherIcons.Clock
    else null