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

package app.vercors.root.setup

import app.vercors.LoggerConfigurator
import app.vercors.appBasePath
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationData
import app.vercors.configuration.ConfigurationService
import ca.gosyer.appdirs.AppDirs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.nio.file.Path

class SetupComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    appDirs: AppDirs = componentContext.inject(),
    private val configurationService: ConfigurationService = componentContext.inject()
) : AbstractAppComponent(componentContext), SetupComponent {
    private val _uiState = MutableStateFlow(
        SetupUiState(
            config = ConfigurationData(),
            path = appDirs.getUserConfigDir()
        )
    )
    override val uiState: StateFlow<SetupUiState> = _uiState

    override fun updatePath(path: String) {
        _uiState.update { it.copy(path = path) }
    }

    override fun openDirectoryPicker() {
        _uiState.update { it.copy(showDirectoryPicker = true) }
    }

    override fun closeDirectoryPicker() {
        _uiState.update { it.copy(showDirectoryPicker = false) }
    }

    override fun updateShowTutorial(showTutorial: Boolean) {
        _uiState.update { it.copy(showTutorial = showTutorial) }
    }

    override fun launch() {
        val currentState = uiState.value
        appBasePath = Path.of(currentState.path)
        LoggerConfigurator.reload()
        configurationService.update(
            currentState.config.copy(showTutorial = currentState.showTutorial),
            forceSave = true
        )
    }
}