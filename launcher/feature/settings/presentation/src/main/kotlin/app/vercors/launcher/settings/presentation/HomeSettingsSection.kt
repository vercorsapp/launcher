/*
 * Copyright (c) 2024 skyecodes
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

package app.vercors.launcher.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.config.model.HomeProviderConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.presentation.ui.AppFilterChip
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.settings.presentation.entry.ComboboxSettingsEntry
import app.vercors.launcher.settings.presentation.entry.ListSettingsEntry

@Composable
fun HomeSettingsSection(
    config: HomeConfigUi,
    onIntent: (SettingsUiIntent) -> Unit
) {
    SettingsSection(title = appStringResource { home }) {
        ComboboxSettingsEntry(
            title = appStringResource { provider },
            description = appStringResource { provider_description },
            options = HomeProviderConfig.entries,
            value = config.provider,
            textConverter = { it.displayName },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = config.provider.icon,
                    contentDescription = config.provider.displayName
                )
            },
            onValueChange = { onIntent(SettingsUiIntent.SelectProvider(it)) }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = it.icon,
                    contentDescription = it.displayName
                )
                Text(text = it.displayName)
            }
        }
        ListSettingsEntry(
            title = appStringResource { sections },
            description = appStringResource { sections_description },
            entries = HomeSectionConfig.entries,
            selectedItems = config.sections
        ) { item, selected ->
            AppFilterChip(
                selected = selected,
                onClick = { onIntent(SettingsUiIntent.ToggleSection(item)) },
                label = { Text(text = item.displayName) }
            )
        }
    }
}

private val HomeProviderConfig.displayName: String
    @Composable get() = appStringResource {
        when (this@displayName) {
            HomeProviderConfig.Modrinth -> modrinth
            HomeProviderConfig.Curseforge -> curseforge
        }
    }

private val HomeProviderConfig.icon: ImageVector
    @Composable get() = appVectorResource {
        when (this@icon) {
            HomeProviderConfig.Modrinth -> modrinth
            HomeProviderConfig.Curseforge -> curseforge
        }
    }

private val HomeSectionConfig.displayName: String
    @Composable get() = appStringResource {
        when (this@displayName) {
            HomeSectionConfig.JumpBackIn -> instances
            HomeSectionConfig.PopularMods -> mods
            HomeSectionConfig.PopularModpacks -> modpacks
            HomeSectionConfig.PopularResourcePacks -> resource_packs
            HomeSectionConfig.PopularShaderPacks -> shader_packs
        }
    }