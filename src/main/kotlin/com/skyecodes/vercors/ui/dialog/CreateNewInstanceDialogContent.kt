package com.skyecodes.vercors.ui.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import com.skyecodes.vercors.applyIf
import com.skyecodes.vercors.component.dialog.CreateNewInstanceDialogComponent
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.resourceAsStream
import com.skyecodes.vercors.ui.LocalConfiguration
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.Box
import compose.icons.feathericons.Feather
import compose.icons.feathericons.Plus
import compose.icons.feathericons.X

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateNewInstanceDialogContent(component: CreateNewInstanceDialogComponent) {
    val uiState by component.uiState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val locale = LocalLocalization.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.padding(UI.largePadding)
            .applyIf(LocalConfiguration.current.animations) { animateContentSize() },
        verticalArrangement = Arrangement.spacedBy(UI.largePadding)
    ) {
        Text(
            text = locale.createNewInstance,
            style = MaterialTheme.typography.h6
        )

        Row(horizontalArrangement = Arrangement.spacedBy(UI.largePadding)) {
            FormField(locale.instanceName, Modifier.weight(1f)) {
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

            FormField(locale.icon) {
                OutlinedFieldButton {
                    Icon(FeatherIcons.Box, null, Modifier.padding(UI.smallPadding).fillMaxSize())
                }
            }
        }

        FormField(locale.minecraftVersion) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppDropdownMenuBox(
                    options = uiState.minecraftVersions,
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
                    text = locale.includeSnapshots,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }

        FormField(locale.loader) {
            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                LoaderChip(null, uiState.loader == null) { component.updateLoader(null) }
                Loader.entries.forEach {
                    LoaderChip(it, uiState.loader == it) { component.updateLoader(it) }
                }
            }
        }

        if (uiState.loader != null) {
            FormField(locale.loaderVersion) {
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
            TextButton(
                onClick = component::close
            ) {
                Icon(
                    imageVector = FeatherIcons.X,
                    contentDescription = null,
                    modifier = Modifier.size(UI.mediumIconSize)
                )
                Text(
                    text = locale.cancel,
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }

            IconTextButton(
                enabled = uiState.isValid,
                onClick = component::createInstance,
                imageVector = FeatherIcons.Plus,
                text = locale.create
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
        Text(name, style = MaterialTheme.typography.subtitle2)
        content()
    }
}

@Composable
private fun LoaderChip(value: Loader?, selected: Boolean, onClick: () -> Unit) {
    value?.let {
        SelectIconChip(
            selected = selected,
            onClick = onClick,
            text = it.text,
            painter = loadSvgPainter(resourceAsStream("/icon/svg/${it.value}.svg"), LocalDensity.current),
        )
    } ?: SelectIconChip(
        selected = selected,
        onClick = onClick,
        text = UI.vanilla,
        imageVector = FeatherIcons.Feather
    )
}
