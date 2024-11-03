package app.vercors.launcher.setup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.setup.domain.repository.SetupRepository
import app.vercors.launcher.setup.presentation.action.SetupAction
import app.vercors.launcher.setup.presentation.state.SetupUiState
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.io.files.Path
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

private val logger = KotlinLogging.logger {}

@KoinViewModel
class SetupViewModel(
    private val setupRepository: SetupRepository,
    @Provided private val onLaunch: () -> Unit
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUiState(setupRepository.defaultPath))
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    fun onAction(action: SetupAction) {
        when (action) {
            is SetupAction.PickDirectory -> pickDirectory(action.path)
            is SetupAction.UpdatePath -> updatePath(action.path)
            SetupAction.StartApp -> launchApp()
        }
    }

    private fun updatePath(path: String) {
        _uiState.update { it.copy(path = path) }
    }

    private fun pickDirectory(path: String) {
        updatePath(if (path.endsWith(APP_NAME)) path else Path(path, APP_NAME).toString())
    }

    private fun launchApp() {
        val path = uiState.value.path
        logger.info { "User selected directory: $path" }
        setupRepository.updatePath(path)
        onLaunch()
    }
}