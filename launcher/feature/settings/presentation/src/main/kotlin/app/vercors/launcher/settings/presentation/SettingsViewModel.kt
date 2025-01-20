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

package app.vercors.launcher.settings.presentation

import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.config.repository.ConfigUpdate
import app.vercors.launcher.core.presentation.mvi.MviViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val configRepository: ConfigRepository
) : MviViewModel<SettingsUiState, SettingsUiIntent, Nothing>(SettingsUiState.Loading) {
    override fun onStart() {
        super.onStart()
        collectInScope(configRepository.observeConfig()) {
            onIntent(ConfigUpdated(it))
        }
    }

    override fun ReductionState.reduce(intent: SettingsUiIntent) =
        when (intent) {
            is SettingsUiIntent.SelectAccent -> updateConfig(ConfigUpdate.General.Accent, intent.value)
            is SettingsUiIntent.SelectTheme -> updateConfig(ConfigUpdate.General.Theme, intent.value)
            is SettingsUiIntent.ToggleGradient -> updateConfig(ConfigUpdate.General.Gradient, intent.value)
            is SettingsUiIntent.ToggleAnimations -> updateConfig(ConfigUpdate.General.Animations, intent.value)
            is SettingsUiIntent.ToggleDecorated -> updateConfig(ConfigUpdate.General.Decorated, intent.value)
            is SettingsUiIntent.SelectDefaultTab -> updateConfig(ConfigUpdate.General.DefaultTab, intent.value)
            is SettingsUiIntent.ToggleSection -> toggleSection(intent.value)
            is SettingsUiIntent.SelectProvider -> updateConfig(ConfigUpdate.Home.Provider, intent.value)
            is ConfigUpdated -> update {
                SettingsUiState.Loaded(
                    general = GeneralConfigUi(intent.config.general),
                    home = HomeConfigUi(intent.config.home),
                )
            }
        }

    private fun ReductionState.toggleSection(section: HomeSectionConfig) =
        update {
            if (this is SettingsUiState.Loaded) {
                updateConfig(
                    ConfigUpdate.Home.Sections,
                    if (section in home.sections) home.sections - section
                    else (home.sections + section).sorted()
                )
            }
            this
        }

    private fun <T> ReductionState.updateConfig(update: ConfigUpdate<T>, value: T) {
        runInScope { configRepository.updateConfig(update, value) }
    }

    @JvmInline
    private value class ConfigUpdated(val config: AppConfig) : SettingsUiIntent
}