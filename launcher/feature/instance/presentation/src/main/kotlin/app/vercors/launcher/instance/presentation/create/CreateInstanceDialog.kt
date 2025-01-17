/*
 * Copyright (c) 2024-2025 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.instance.presentation.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.dialog.AppDialogCloseButton
import app.vercors.launcher.core.presentation.dialog.AppDialogContainer
import app.vercors.launcher.core.presentation.form.AppForm
import app.vercors.launcher.core.presentation.form.AppFormItem
import app.vercors.launcher.core.presentation.mvi.MviContainer
import app.vercors.launcher.core.presentation.ui.AppDropdownMenuBox
import app.vercors.launcher.core.presentation.ui.AppFilterChip
import app.vercors.launcher.core.presentation.ui.AppIconButton
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.game.domain.loader.ModLoaderType
import app.vercors.launcher.game.presentation.loader.displayName
import app.vercors.launcher.game.presentation.loader.icon
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateInstanceDialog(
    onClose: () -> Unit,
) {
    MviContainer(
        viewModel = koinViewModel<CreateInstanceViewModel>(),
        onEffect = {
            when (it) {
                CreateInstanceUiEffect.CloseDialog -> onClose()
            }
        }
    ) { state, onIntent ->
        CreateInstanceDialog(
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
private fun CreateInstanceDialog(
    state: CreateInstanceUiState,
    onIntent: (CreateInstanceUiIntent) -> Unit,
) {
    AppDialogContainer(
        title = appStringResource { create_new_instance },
        buttons = {
            AppIconButton(
                onClick = { onIntent(CreateInstanceUiIntent.CreateInstance) },
                icon = appVectorResource { plus },
                text = appStringResource { create },
                enabled = state.isValid
            )
            AppDialogCloseButton { onIntent(CreateInstanceUiIntent.CloseDialog) }
        }
    ) {
        AppForm {
            AppFormItem {
                OutlinedTextField(
                    label = { Text("Name") },
                    value = state.instanceName,
                    onValueChange = { onIntent(CreateInstanceUiIntent.UpdateInstanceName(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AppFormItem {
                GameVersionSelector(
                    filteredGameVersions = state.filteredGameVersions,
                    selectedGameVersion = state.selectedGameVersion,
                    showSnapshots = state.showSnapshots,
                    onSelect = { onIntent(CreateInstanceUiIntent.SelectGameVersion(it.id)) },
                    onToggleShowSnapshots = { onIntent(CreateInstanceUiIntent.ToggleShowSnapshots(it)) }
                )
            }
            AppFormItem {
                LoaderSelector(
                    modLoader = state.modLoader,
                    modLoaderVersions = state.modLoaderVersions,
                    onSelect = { onIntent(CreateInstanceUiIntent.SelectModLoader(it)) })
            }
            if (state.modLoader != null) {
                AppFormItem {
                    LoaderVersionSelector(
                        modLoader = state.modLoader,
                        modLoaderVersions = state.modLoaderVersions,
                        selectedModLoaderVersion = state.selectedModLoaderVersion,
                        onSelect = { onIntent(CreateInstanceUiIntent.SelectModLoaderVersion(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GameVersionSelector(
    filteredGameVersions: List<GameVersionUi>?,
    selectedGameVersion: GameVersionUi?,
    showSnapshots: Boolean,
    onSelect: (GameVersionUi) -> Unit,
    onToggleShowSnapshots: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!filteredGameVersions.isNullOrEmpty()) {
            AppDropdownMenuBox(
                options = filteredGameVersions,
                value = selectedGameVersion,
                label = { Text(text = appStringResource { game_version }) },
                textConverter = { it.displayName },
                onValueChange = { onSelect(it) },
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = it.displayName)
                    it.icon?.let { icon ->
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = icon,
                            contentDescription = it.displayName
                        )
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = appStringResource { loading },
                onValueChange = {},
                label = { Text(text = appStringResource { game_version }) },
                enabled = false,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = showSnapshots,
                onCheckedChange = onToggleShowSnapshots
            )
            Text(text = appStringResource { show_snapshots })
        }
    }
}

@Composable
private fun LoaderSelector(
    modLoader: ModLoaderType?,
    modLoaderVersions: Map<ModLoaderType, List<String>>?,
    onSelect: (ModLoaderType?) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        (listOf(null) + ModLoaderType.entries).forEach {
            AppFilterChip(
                selected = modLoader == it,
                onClick = { onSelect(it) },
                enabled = it == null || modLoaderVersions?.contains(it) == true,
                label = { Text(text = it.displayName) },
                leadingIcon = { Icon(imageVector = it.icon, contentDescription = it.displayName) },
            )
        }
    }
}

@Composable
private fun ColumnScope.LoaderVersionSelector(
    modLoader: ModLoaderType,
    modLoaderVersions: Map<ModLoaderType, List<String>>?,
    selectedModLoaderVersion: String?,
    onSelect: (String) -> Unit,
) {
    if (modLoaderVersions != null) {
        AppDropdownMenuBox(
            options = modLoaderVersions[modLoader]!!,
            value = selectedModLoaderVersion,
            label = { Text(text = appStringResource { loader_version }) },
            textConverter = { it },
            onValueChange = { onSelect(it) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = it)
            }
        }
    } else {
        OutlinedTextField(
            value = appStringResource { loading },
            onValueChange = {},
            label = { Text(text = appStringResource { loader_version }) },
            enabled = false,
            modifier = Modifier.weight(1f)
        )
    }
}

private val GameVersionUi.displayName: String
    @Composable get() = when {
        isLatestRelease -> appStringResource(id) { latest_release }
        isLatestSnapshot -> appStringResource(id) { latest_snapshot }
        else -> id
    }

private val GameVersionUi.icon: ImageVector?
    @Composable get() = when {
        isLatestRelease -> appVectorResource { star }
        isLatestSnapshot -> appVectorResource { bug }
        else -> null
    }