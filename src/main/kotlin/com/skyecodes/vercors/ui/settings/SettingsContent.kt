package com.skyecodes.vercors.ui.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.skyecodes.vercors.component.screen.SettingsComponent
import com.skyecodes.vercors.data.model.app.AppColor
import com.skyecodes.vercors.data.model.app.AppTab
import com.skyecodes.vercors.data.model.app.AppTheme
import com.skyecodes.vercors.data.model.app.Provider
import com.skyecodes.vercors.resourceAsStream
import com.skyecodes.vercors.ui.LocalConfiguration
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.ScrollableExposedDropdownMenu
import com.skyecodes.vercors.ui.common.SectionContent
import com.skyecodes.vercors.ui.common.SelectIconChip
import com.skyecodes.vercors.ui.common.loadSvgPainter
import compose.icons.FeatherIcons
import compose.icons.feathericons.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsContent(component: SettingsComponent) {
    val configuration = LocalConfiguration.current
    val locale = LocalLocalization.current

    Box(Modifier.padding(start = UI.mediumPadding)) {
        val scrollState = rememberScrollState()

        Column(
            Modifier.fillMaxSize()
                .padding(top = UI.smallPadding, bottom = UI.smallPadding, end = UI.mediumPadding + 6.dp)
                .verticalScroll(scrollState)
        ) {
            SectionContent(locale.userInterface) {
                Card {
                    Column(
                        modifier = Modifier.padding(UI.largePadding),
                        verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                    ) {
                        Setting(locale.theme, locale.themeDescription) {
                            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                                AppTheme.entries.forEach {
                                    SelectIconChip(
                                        selected = it === configuration.theme,
                                        onClick = {
                                            if (it !== configuration.theme) component.onConfigChange(
                                                configuration.copy(theme = it)
                                            )
                                        },
                                        text = it.text,
                                        imageVector = it.icon
                                    )
                                }
                            }
                        }

                        Setting(locale.accentColor, locale.accentColorDescription) {
                            var expanded by remember { mutableStateOf(false) }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it }
                            ) {
                                OutlinedTextField(
                                    value = configuration.accentColor.title,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.pointerHoverIcon(
                                        PointerIcon.Default,
                                        overrideDescendants = true
                                    ),
                                    leadingIcon = {
                                        Box(
                                            Modifier.size(UI.mediumIconSize)
                                                .background(configuration.accentColor.ofPalette(LocalPalette.current))
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (expanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                            contentDescription = "Show options"
                                        )
                                    }
                                )

                                ScrollableExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                ) {
                                    AppColor.entries.forEach {
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded = false
                                                component.onConfigChange(configuration.copy(accentColor = it))
                                            },
                                            contentPadding = PaddingValues(horizontal = 10.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Box(
                                                    Modifier.size(UI.mediumIconSize)
                                                        .background(it.ofPalette(LocalPalette.current))
                                                )
                                                Text(it.title)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Setting(locale.systemWindow, locale.systemWindowDescription) {
                            Switch(
                                checked = configuration.useSystemWindowFrame,
                                onCheckedChange = { component.onConfigChange(configuration.copy(useSystemWindowFrame = it)) }
                            )
                        }

                        Setting(locale.animations, locale.animationsDescription) {
                            Switch(
                                checked = configuration.animations,
                                onCheckedChange = { component.onConfigChange(configuration.copy(animations = it)) }
                            )
                        }

                        Setting(locale.defaultTab, locale.defaultTabDescription) {
                            var expanded by remember { mutableStateOf(false) }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it }
                            ) {
                                OutlinedTextField(
                                    value = configuration.defaultTab.localizedTitle(locale),
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.pointerHoverIcon(
                                        PointerIcon.Default,
                                        overrideDescendants = true
                                    ),
                                    leadingIcon = { Icon(configuration.defaultTab.icon, null) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (expanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                                            contentDescription = "Show options"
                                        )
                                    }
                                )

                                ScrollableExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    showScrollbar = false
                                ) {
                                    AppTab.entries.forEach {
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded = false
                                                component.onConfigChange(configuration.copy(defaultTab = it))
                                            },
                                            contentPadding = PaddingValues(horizontal = 10.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Icon(it.icon, null, Modifier.size(UI.mediumIconSize))
                                                Text(it.localizedTitle(locale))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Setting(locale.providers, locale.providersDescription) {
                            Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                                Provider.entries.forEach {
                                    SelectIconChip(
                                        selected = it in configuration.homeProviders,
                                        onClick = { component.onHomeProviderChanged(it, configuration) },
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
