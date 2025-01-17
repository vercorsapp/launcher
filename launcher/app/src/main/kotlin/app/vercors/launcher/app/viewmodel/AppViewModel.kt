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

package app.vercors.launcher.app.viewmodel

import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.Job
import org.koin.core.annotation.Single

@Single
class AppViewModel(
    private val configRepository: ConfigRepository
) : MviViewModel<AppUiState, AppUiIntent, Nothing>(AppUiState()) {
    private var configJob: Job? = null

    override fun onStart() {
        super.onStart()
        configJob?.cancel()
        configJob = collectInScope(configRepository.observeConfig()) {
            onIntent(ConfigUpdated(it))
        }
    }

    override fun onStop(cause: Throwable?) {
        super.onStop(cause)
        configJob?.cancel()
    }

    override fun AppUiState.reduce(intent: AppUiIntent) = when (intent) {
        AppUiIntent.CloseDialog -> copy(currentDialog = null)
        is AppUiIntent.OpenDialog -> copy(currentDialog = intent.dialog)
        is ConfigUpdated -> copy(generalConfig = generalConfig.updateConfigState(intent.config.general))
    }

    private fun GeneralConfigState.updateConfigState(config: GeneralConfig): GeneralConfigState = when (this) {
        is GeneralConfigState.Loaded -> copy(
            theme = config.theme,
            accent = config.accent,
            gradient = config.gradient,
            animations = config.animations
        )

        GeneralConfigState.Loading -> GeneralConfigState.Loaded(
            theme = config.theme,
            accent = config.accent,
            gradient = config.gradient,
            decorated = config.decorated,
            animations = config.animations,
            defaultTab = config.defaultTab
        )
    }

    @JvmInline
    private value class ConfigUpdated(val config: AppConfig) : AppUiIntent
}