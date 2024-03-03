package app.vercors.configuration

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ConfigurationContent(component: ConfigurationComponent) {
    val configuration = LocalConfiguration.current

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
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).padding(2.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
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

@OptIn(ExperimentalResourceApi::class)
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