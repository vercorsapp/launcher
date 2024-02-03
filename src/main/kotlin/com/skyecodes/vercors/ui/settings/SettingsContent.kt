package com.skyecodes.vercors.ui.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.component.screen.SettingsComponent
import com.skyecodes.vercors.data.model.app.*
import com.skyecodes.vercors.resourceAsStream
import com.skyecodes.vercors.ui.LocalConfiguration
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.AppDropdownMenuBox
import com.skyecodes.vercors.ui.common.AppSwitch
import com.skyecodes.vercors.ui.common.SectionContent
import com.skyecodes.vercors.ui.common.SelectIconChip
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check


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
            SettingsSection(locale.userInterface) {
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
                                text = it.localizedText(locale),
                                imageVector = it.icon
                            )
                        }
                    }
                }

                Setting(locale.accentColor, locale.accentColorDescription) {
                    AppDropdownMenuBox(
                        options = AppColor.entries,
                        value = configuration.accentColor,
                        textConverter = AppColor::title,
                        onValueChange = { component.onConfigChange(configuration.copy(accentColor = it)) },
                        leadingIcon = {
                            Box(
                                Modifier.size(UI.mediumIconSize)
                                    .background(configuration.accentColor.ofPalette(LocalPalette.current))
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
                                        .background(it.ofPalette(LocalPalette.current))
                                )
                            }
                            Text(it.title)
                        }
                    }
                }

                Setting(locale.systemWindow, locale.systemWindowDescription) {
                    AppSwitch(
                        checked = configuration.useSystemWindowFrame,
                        onCheckedChange = { component.onConfigChange(configuration.copy(useSystemWindowFrame = it)) }
                    )
                }

                Setting(locale.animations, locale.animationsDescription) {
                    AppSwitch(
                        checked = configuration.animations,
                        onCheckedChange = { component.onConfigChange(configuration.copy(animations = it)) }
                    )
                }

                Setting(locale.defaultTab, locale.defaultTabDescription) {
                    AppDropdownMenuBox(
                        options = AppTab.entries,
                        value = configuration.defaultTab,
                        textConverter = { it.localizedTitle(locale) },
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
                            Text(it.localizedTitle(locale))
                        }
                    }
                }
            }

            SettingsSection(locale.home) {
                Setting(locale.sections, locale.sectionsDescription) { // TODO allow reordering home sections
                    Row(horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)) {
                        HomeSectionType.entries.forEach {
                            SelectIconChip(
                                selected = it in configuration.homeSections,
                                onClick = { component.onHomeSectionChanged(it, configuration) },
                                text = it.localizedShortTitle(locale)
                            )
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

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@Composable
private fun SettingsSection(name: String, content: @Composable ColumnScope.() -> Unit) {
    SectionContent(name) {
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
    title: String,
    description: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(UI.smallPadding)) {
            Text(title, style = MaterialTheme.typography.h6)
            Text(description)
        }
        content()
    }
}
