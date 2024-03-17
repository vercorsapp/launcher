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

package app.vercors.configuration

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.LocalConfiguration
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.color
import app.vercors.common.*
import app.vercors.home.HomeSectionType
import app.vercors.home.shortTitle
import app.vercors.project.ProjectProviderType
import app.vercors.project.icon
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check
import compose.icons.feathericons.Folder
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@Composable
fun ConfigurationContent(component: ConfigurationComponent) {
    val configuration = LocalConfiguration.current
    val uiState by component.uiState.collectAsState()

    Box(Modifier.padding(start = UI.mediumPadding)) {
        val scrollState = rememberScrollState()

        Column(
            Modifier.fillMaxSize()
                .padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.mediumPadding + 6.dp)
                .verticalScroll(scrollState)
        ) {
            SettingsSection(Res.string.userInterface) {
                Setting(Res.string.theme, Res.string.themeDescription) {
                    Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                        AppTheme.entries.forEach {
                            SelectIconChip(
                                selected = it === configuration.theme,
                                onClick = { component.onConfigChange(configuration.copy(theme = it)) },
                                text = it.title,
                                imageVector = it.icon
                            )
                        }
                    }
                }

                if (configuration.theme != AppTheme.Light) {
                    Setting(Res.string.darkTheme, Res.string.darkThemeDescription) {
                        Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                            AppDarkTheme.entries.forEach {
                                SelectIconChip(
                                    selected = it === configuration.darkTheme,
                                    onClick = { component.onConfigChange(configuration.copy(darkTheme = it)) },
                                    text = it.title
                                )
                            }
                        }
                    }
                }

                Setting(Res.string.accentColor, Res.string.accentColorDescription) {
                    AppDropdownMenuBox(
                        options = AppColor.entries,
                        value = configuration.accentColor,
                        textConverter = { it.title },
                        onValueChange = { component.onConfigChange(configuration.copy(accentColor = it)) },
                        leadingIcon = {
                            Box(
                                Modifier.size(UI.mediumIconSize)
                                    .background(
                                        configuration.accentColor.ofPalette(LocalPalette.current.original).color()
                                    )
                            )
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (it == configuration.accentColor) {
                                Icon(FeatherIcons.Check, null, Modifier.size(UI.mediumIconSize))
                            } else {
                                Box(
                                    Modifier.size(UI.mediumIconSize)
                                        .background(it.ofPalette(LocalPalette.current.original).color())
                                )
                            }
                            Text(it.title)
                        }
                    }
                }

                Setting(Res.string.systemWindow, Res.string.systemWindowDescription) {
                    AppSwitch(
                        checked = configuration.useSystemWindowFrame,
                        onCheckedChange = { component.onConfigChange(configuration.copy(useSystemWindowFrame = it)) }
                    )
                }

                Setting(Res.string.animations, Res.string.animationsDescription) {
                    AppSwitch(
                        checked = configuration.animations,
                        onCheckedChange = { component.onConfigChange(configuration.copy(animations = it)) }
                    )
                }

                Setting(Res.string.defaultTab, Res.string.defaultTabDescription) {
                    AppDropdownMenuBox(
                        options = AppTab.entries,
                        value = configuration.defaultTab,
                        textConverter = { it.title },
                        onValueChange = { component.onConfigChange(configuration.copy(defaultTab = it)) },
                        leadingIcon = { Icon(configuration.defaultTab.icon, null) },
                        showScrollbar = false
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(it.icon, null, Modifier.size(UI.mediumIconSize))
                            Text(it.title)
                        }
                    }
                }
            }

            SettingsSection(Res.string.home) {
                Setting(Res.string.sections, Res.string.sectionsDescription) { // TODO allow reordering home sections
                    Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                        HomeSectionType.entries.forEach {
                            SelectIconChip(
                                selected = it in configuration.homeSections,
                                onClick = { component.onHomeSectionChanged(it, configuration) },
                                text = it.shortTitle
                            )
                        }
                    }
                }

                Setting(Res.string.providers, Res.string.providersDescription) {
                    Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                        ProjectProviderType.entries.forEach {
                            SelectIconChip(
                                selected = it in configuration.homeProviders,
                                onClick = { component.onHomeProviderChanged(it, configuration) },
                                text = it.text,
                                imageVector = it.icon
                            )
                        }
                    }
                }
            }

            SettingsSection(Res.string.java) {
                JavaPathSetting(
                    title = Res.string.java8Path,
                    description = Res.string.java8PathDescription,
                    value = configuration.java8Path,
                    onValueChange = { component.onConfigChange(configuration.copy(java8Path = it)) },
                    onOpenDirectoryPicker = { component.openDirectoryPicker(configuration.java8Path) { copy(java8Path = it) } },
                    status = uiState.java8Status,
                    javaVersion = 8
                )

                JavaPathSetting(
                    title = Res.string.java17Path,
                    description = Res.string.java17PathDescription,
                    value = configuration.java17Path,
                    onValueChange = { component.onConfigChange(configuration.copy(java17Path = it)) },
                    onOpenDirectoryPicker = { component.openDirectoryPicker(configuration.java17Path) { copy(java17Path = it) } },
                    status = uiState.java17Status,
                    javaVersion = 17
                )

                Setting(Res.string.defaultAllocatedRam, Res.string.defaultAllocatedRamDescription) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppCheckbox(
                            checked = uiState.hasCustomMemory,
                            onCheckedChange = { component.toggleCustomMemory() }
                        )
                        Slider(
                            enabled = uiState.hasCustomMemory,
                            value = uiState.currentMemory.toFloat(),
                            onValueChange = { component.onCustomMemoryChange(it.toInt()) },
                            valueRange = 0f..uiState.totalMemory.toFloat(),
                            modifier = Modifier.width(428.dp)
                        )
                        OutlinedTextField(
                            enabled = uiState.hasCustomMemory,
                            value = uiState.currentMemory.toString(),
                            onValueChange = { it.toIntOrNull()?.let { v -> component.onCustomMemoryChange(v, false) } },
                            modifier = Modifier.width(150.dp),
                            trailingIcon = { Text("MB") }
                        )
                    }
                }

                Setting(Res.string.jvmArguments, Res.string.jvmArgumentsDescription) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = configuration.jvmArguments,
                            onValueChange = { component.onConfigChange(configuration.copy(jvmArguments = it)) },
                            modifier = Modifier.width(632.dp)
                        )
                    }
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }

    DirectoryPicker(
        show = uiState.showDirectoryPicker,
        initialDirectory = uiState.initialPath
    ) { path ->
        path?.let { uiState.apply { component.onConfigChange(configuration.onSelectPath(it)) } }
        component.closeDirectoryPicker()
    }
}

@Composable
private fun SettingsSection(name: StringResource, content: @Composable ColumnScope.() -> Unit) {
    SectionContent(stringResource(name)) {
        Card {
            Column(
                modifier = Modifier.padding(UI.largePadding).padding(),
                verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun Setting(
    title: StringResource,
    description: StringResource,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(UI.smallPadding)) {
            Text(stringResource(title), style = MaterialTheme.typography.h6)
            Text(stringResource(description))
        }
        content()
    }
}

@Composable
private fun JavaPathSetting(
    title: StringResource,
    description: StringResource,
    value: String?,
    onValueChange: (String) -> Unit,
    onOpenDirectoryPicker: () -> Unit,
    status: ConfigurationJavaStatus,
    javaVersion: Int
) {
    Setting(title, description) {
        Column(verticalArrangement = Arrangement.spacedBy(UI.smallPadding)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = value ?: "",
                    onValueChange = onValueChange,
                    modifier = Modifier.width(500.dp)
                )
                IconTextButton(
                    onClick = onOpenDirectoryPicker,
                    imageVector = FeatherIcons.Folder,
                    text = stringResource(Res.string.browse),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.smallPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = status.icon,
                    contentDescription = status.name,
                    modifier = Modifier.size(UI.mediumIconSize),
                    tint = status.color
                )
                Text(
                    text = status.text(javaVersion),
                    color = status.color,
                    lineHeight = UI.normalLineHeight
                )
            }
        }
    }
}
