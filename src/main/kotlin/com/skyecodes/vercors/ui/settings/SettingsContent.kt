package com.skyecodes.vercors.ui.settings

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.data.model.app.AppScene
import com.skyecodes.vercors.data.model.app.AppTheme
import com.skyecodes.vercors.data.model.app.Provider
import com.skyecodes.vercors.logic.SettingsViewModel
import com.skyecodes.vercors.resourceAsStream
import com.skyecodes.vercors.ui.LocalConfiguration
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.SectionContent
import com.skyecodes.vercors.ui.common.SelectIconChip
import com.skyecodes.vercors.ui.common.loadSvgPainter
import compose.icons.FeatherIcons
import compose.icons.feathericons.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsContent(viewModel: SettingsViewModel) {
    val configuration = LocalConfiguration.current

    Box(Modifier.padding(start = UI.mediumPadding)) {
        val scrollState = rememberScrollState()

        Column(
            Modifier.fillMaxSize()
                .padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.mediumPadding + 6.dp)
                .verticalScroll(scrollState)
        ) {
            SectionContent(UI.Text.USER_INTERFACE) {
                Card {
                    Column(
                        modifier = Modifier.padding(UI.largePadding),
                        verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                    ) {
                        Setting(UI.Text.THEME, UI.Text.THEME_DESCRIPTION) {
                            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                                AppTheme.entries.forEach {
                                    SelectIconChip(
                                        selected = it === configuration.theme,
                                        onClick = {
                                            if (it !== configuration.theme) viewModel.onConfigChange(
                                                configuration.copy(theme = it)
                                            )
                                        },
                                        text = it.text,
                                        imageVector = it.icon
                                    )
                                }
                            }
                        }

                        Setting(UI.Text.SYSTEM_WINDOW, UI.Text.SYSTEM_WINDOW_DESCRIPTION) {
                            Switch(
                                checked = configuration.useSystemWindowFrame,
                                onCheckedChange = { viewModel.onConfigChange(configuration.copy(useSystemWindowFrame = it)) }
                            )
                        }

                        Setting(UI.Text.ANIMATIONS, UI.Text.ANIMATIONS_DESCRIPTION) {
                            Switch(
                                checked = configuration.animations,
                                onCheckedChange = { viewModel.onConfigChange(configuration.copy(animations = it)) }
                            )
                        }

                        Setting(UI.Text.DEFAULT_TAB, UI.Text.DEFAULT_TAB_DESCRIPTION) {
                            var isDefaultTabDropdownMenuExpanded by remember { mutableStateOf(false) }

                            ExposedDropdownMenuBox(
                                expanded = isDefaultTabDropdownMenuExpanded,
                                onExpandedChange = { isDefaultTabDropdownMenuExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = configuration.defaultScene.title,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.pointerHoverIcon(
                                        PointerIcon.Default,
                                        overrideDescendants = true
                                    ),
                                    leadingIcon = { Icon(configuration.defaultScene.icon, null) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (isDefaultTabDropdownMenuExpanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                            contentDescription = "Show options"
                                        )
                                    }
                                )

                                ExposedDropdownMenu(
                                    expanded = isDefaultTabDropdownMenuExpanded,
                                    onDismissRequest = { isDefaultTabDropdownMenuExpanded = false }
                                ) {
                                    AppScene.entries.forEach {
                                        DropdownMenuItem(
                                            onClick = {
                                                isDefaultTabDropdownMenuExpanded = false
                                                viewModel.onConfigChange(configuration.copy(defaultScene = it))
                                            },
                                            contentPadding = PaddingValues(horizontal = 10.dp)
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
                            }
                        }

                        Setting(UI.Text.PROVIDERS, UI.Text.PROVIDERS_DESCRIPTION) {
                            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                                Provider.entries.forEach {
                                    SelectIconChip(
                                        selected = it in configuration.homeProviders,
                                        onClick = { viewModel.onHomeProviderChanged(it, configuration) },
                                        text = it.text,
                                        painter = loadSvgPainter(
                                            resourceAsStream("/icon/svg/${it.value}.svg"),
                                            LocalDensity.current
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@Composable
private fun Setting(
    title: String,
    description: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(UI.smallPadding)) {
            Text(title, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.ExtraBold)
            Text(description)
        }
        content()
    }
}

@Stable
private val AppTheme.text: String
    get() = when (this) {
        AppTheme.SYSTEM -> "System"
        AppTheme.LIGHT -> "Light"
        AppTheme.DARK -> "Dark"
    }

@Stable
private val AppTheme.icon: ImageVector
    get() = when (this) {
        AppTheme.SYSTEM -> FeatherIcons.Cpu
        AppTheme.LIGHT -> FeatherIcons.Sun
        AppTheme.DARK -> FeatherIcons.Moon
    }

@Stable
private val Provider.text: String
    get() = when (this) {
        Provider.Modrinth -> "Modrinth"
        Provider.Curseforge -> "Curseforge"
    }
