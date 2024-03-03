package app.vercors.root.setup

import app.vercors.common.AbstractAppComponent
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationData
import app.vercors.configuration.ConfigurationService
import app.vercors.system.storage.StorageService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.io.path.absolutePathString

class SetupComponentImpl(
    componentContext: app.vercors.common.AppComponentContext,
    override val onClose: () -> Unit,
    config: ConfigurationData,
    storageService: StorageService = componentContext.inject(),
    private val configurationService: ConfigurationService = componentContext.inject()
) : AbstractAppComponent(componentContext), SetupComponent {
    private val _uiState = MutableStateFlow(
        SetupUiState(
            config = config,
            path = storageService.defaultBasePath.absolutePathString()
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
        configurationService.update(
            currentState.config.copy(
                path = currentState.path,
                showTutorial = currentState.showTutorial
            )
        )
    }
}