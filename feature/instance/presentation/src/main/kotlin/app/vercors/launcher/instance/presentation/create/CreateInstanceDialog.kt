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
internal fun CreateInstanceDialog(
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!state.filteredGameVersions.isNullOrEmpty()) {
                        AppDropdownMenuBox(
                            options = state.filteredGameVersions,
                            value = state.selectedGameVersion,
                            label = { Text(text = appStringResource { game_version }) },
                            textConverter = { it.displayName },
                            onValueChange = { onIntent(CreateInstanceUiIntent.SelectGameVersion(it.id)) },
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
                            value = "Loading...",
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
                            checked = state.showSnapshots,
                            onCheckedChange = { onIntent(CreateInstanceUiIntent.ToggleShowSnapshots(it)) }
                        )
                        Text("Show snapshots")
                    }
                }
            }
            AppFormItem {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    (listOf(null) + ModLoaderType.entries).forEach {
                        AppFilterChip(
                            selected = state.modLoader == it,
                            onClick = { onIntent(CreateInstanceUiIntent.SelectModLoader(it)) },
                            label = { Text(text = it.displayName) },
                            leadingIcon = { Icon(imageVector = it.icon, contentDescription = it.displayName) },
                        )
                    }
                }
            }
            if (state.modLoader != null) {
                AppFormItem {
                    if (state.modLoaderVersions != null) {
                        AppDropdownMenuBox(
                            options = state.modLoaderVersions,
                            value = state.selectedModLoaderVersion,
                            label = { Text(text = appStringResource { loader_version }) },
                            textConverter = { it },
                            onValueChange = { onIntent(CreateInstanceUiIntent.SelectModLoaderVersion(it)) },
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
                            value = "Loading...",
                            onValueChange = {},
                            label = { Text(text = appStringResource { loader_version }) },
                            enabled = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

val GameVersionUi.displayName: String
    @Composable get() = when {
        isLatestRelease -> appStringResource(id) { latest_release }
        isLatestSnapshot -> appStringResource(id) { latest_snapshot }
        else -> id
    }

val GameVersionUi.icon: ImageVector?
    @Composable get() = when {
        isLatestRelease -> appVectorResource { star }
        isLatestSnapshot -> appVectorResource { bug }
        else -> null
    }