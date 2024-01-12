package com.skyecodes.snowball.ui.instances

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.skyecodes.snowball.data.app.ModLoader
import com.skyecodes.snowball.data.mojang.MojangReleaseType
import com.skyecodes.snowball.data.mojang.MojangVersionManifest
import com.skyecodes.snowball.service.MojangService
import com.skyecodes.snowball.ui.UI
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateInstanceDialog(onClose: () -> Unit) {
    val mojangService: MojangService by rememberDI { instance() }
    val interactionSource = remember { MutableInteractionSource() }
    var versionManifest: MojangVersionManifest? by remember { mutableStateOf(null) }
    var instanceName by remember { mutableStateOf("") }
    var isVersionDropdownMenuExpanded by remember { mutableStateOf(false) }
    var minecraftVersion by remember { mutableStateOf("") }
    var includeSnapshots by remember { mutableStateOf(false) }
    var isModloaderDropdownMenuExpanded by remember { mutableStateOf(false) }
    var modLoader by remember { mutableStateOf(ModLoader.Vanilla) }

    LaunchedEffect(true) {
        val manifest = mojangService.getVersionManifest()
        versionManifest = manifest
        minecraftVersion = manifest.latest.release
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

                Column(verticalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                    Text(UI.Text.INSTANCE_NAME, style = MaterialTheme.typography.subtitle2)
                    OutlinedTextField(
                        value = instanceName,
                        onValueChange = { instanceName = it },
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                    Text(
                        text = UI.Text.MINECRAFT_VERSION,
                        style = MaterialTheme.typography.subtitle2
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ExposedDropdownMenuBox(
                            expanded = isVersionDropdownMenuExpanded,
                            onExpandedChange = { isVersionDropdownMenuExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = minecraftVersion,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Default, overrideDescendants = true),
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (isVersionDropdownMenuExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                        contentDescription = "Show options"
                                    )
                                }
                            )

                            ExposedDropdownMenu(
                                expanded = isVersionDropdownMenuExpanded,
                                onDismissRequest = { isVersionDropdownMenuExpanded = false }
                            ) {
                                versionManifest?.let { manifest ->
                                    manifest.versions
                                        .filter { version -> version.type == MojangReleaseType.Release || includeSnapshots }
                                        .forEach { version ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    minecraftVersion = version.id
                                                    isVersionDropdownMenuExpanded = false
                                                },
                                                contentPadding = PaddingValues(horizontal = 10.dp)
                                            ) {
                                                Text(version.id)
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

                Column(verticalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                    Text(
                        text = UI.Text.MOD_LOADER,
                        style = MaterialTheme.typography.subtitle2
                    )

                    ExposedDropdownMenuBox(
                        expanded = isModloaderDropdownMenuExpanded,
                        onExpandedChange = { isModloaderDropdownMenuExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = modLoader.name,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Default, overrideDescendants = true),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (isModloaderDropdownMenuExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                    contentDescription = "Show options"
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = isModloaderDropdownMenuExpanded,
                            onDismissRequest = { isModloaderDropdownMenuExpanded = false }
                        ) {
                            ModLoader.entries.forEach { loader ->
                                DropdownMenuItem(
                                    onClick = {
                                        modLoader = loader
                                        isModloaderDropdownMenuExpanded = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text(loader.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
