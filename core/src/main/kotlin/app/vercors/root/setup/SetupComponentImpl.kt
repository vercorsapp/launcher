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