package app.vercors.dialog.instance

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.common.*
import app.vercors.project.ModLoader
import app.vercors.project.icon
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Feather
import compose.icons.feathericons.Plus
import compose.icons.feathericons.X
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@Composable
fun CreateInstanceDialogContent(component: CreateInstanceDialogComponent) {
    val uiState by component.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.padding(UI.largePadding).width(570.dp).appAnimateContentSize(),
        verticalArrangement = Arrangement.spacedBy(UI.largePadding)
    ) {
        Text(
            text = stringResource(Res.string.createInstance),
            style = MaterialTheme.typography.h5
        )

        Row(horizontalArrangement = Arrangement.spacedBy(UI.largePadding)) {
            FormField(stringResource(Res.string.instanceName), Modifier.weight(1f)) {
                OutlinedTextField(
                    value = uiState.instanceName,
                    onValueChange = component::updateInstanceName,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).onPreviewKeyEvent {
                        if (uiState.isValid && it.type == KeyEventType.KeyUp && (it.key == Key.Enter || it.key == Key.NumPadEnter)) {
                            component.createInstance()
                            true
                        } else false
                    }
                )
            }

            FormField(stringResource(Res.string.icon)) {
                OutlinedFieldButton {
                    Icon(FeatherIcons.Box, null, Modifier.padding(UI.smallPadding).fillMaxSize())
                }
            }
        }

        FormField(stringResource(Res.string.minecraftVersion)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppDropdownMenuBox(
                    options = uiState.filteredMinecraftVersions,
                    value = uiState.minecraftVersion,
                    textConverter = { it.text },
                    onValueChange = component::updateMinecraftVersion,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(it.text)
                        it.icon?.let {
                            Icon(it, null, Modifier.size(UI.mediumIconSize))
                        }
                    }
                }

                AppLabeledCheckbox(
                    modifier = Modifier.padding(start = UI.smallPadding),
                    checked = uiState.includeSnapshots,
                    onCheckedChange = component::updateIncludeSnapshots,
                    text = stringResource(Res.string.includeSnapshots)
                )
            }
        }

        FormField(stringResource(Res.string.loader)) {
            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                LoaderChip(null, uiState.loader == null) { component.updateLoader(null) }
                ModLoader.entries.forEach {
                    LoaderChip(it, uiState.loader == it) { component.updateLoader(it) }
                }
            }
        }

        if (uiState.loader != null) {
            FormField(stringResource(Res.string.loaderVersion)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppDropdownMenuBox(
                        options = emptyList(),
                        value = null,
                        textConverter = { "" },
                        onValueChange = {},
                        modifier = Modifier.weight(1f)
                    ) {

                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            AppTextButton(
                onClick = component.onClose
            ) {
                Icon(
                    imageVector = FeatherIcons.X,
                    contentDescription = null,
                    modifier = Modifier.size(UI.mediumIconSize)
                )
                Text(
                    text = stringResource(Res.string.cancel),
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }

            IconTextButton(
                enabled = uiState.isValid,
                onClick = component::createInstance,
                imageVector = FeatherIcons.Plus,
                text = stringResource(Res.string.create)
            )
        }
    }
}

@Composable
private fun FormField(name: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UI.mediumPadding)
    ) {
        Text(name, style = MaterialTheme.typography.h6)
        content()
    }
}

@Composable
private fun LoaderChip(value: ModLoader?, selected: Boolean, onClick: () -> Unit) {
    value?.let {
        SelectIconChip(
            selected = selected,
            onClick = onClick,
            text = it.text,
            imageVector = it.icon,
        )
    } ?: SelectIconChip(
        selected = selected,
        onClick = onClick,
        text = ModLoader.Vanilla,
        imageVector = FeatherIcons.Feather
    )
}
