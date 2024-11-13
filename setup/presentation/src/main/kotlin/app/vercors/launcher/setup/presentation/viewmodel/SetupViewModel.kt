package app.vercors.launcher.setup.presentation.viewmodel

import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.core.presentation.viewmodel.MviViewModel
import app.vercors.launcher.core.presentation.viewmodel.StateResult
import app.vercors.launcher.setup.domain.repository.SetupRepository
import kotlinx.io.files.Path
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SetupViewModel(
    private val setupRepository: SetupRepository
) : MviViewModel<SetupUiState, SetupUiEvent, SetupUiEffect>(SetupUiState(setupRepository.defaultPath)) {
    override fun reduce(state: SetupUiState, event: SetupUiEvent): StateResult<SetupUiState, SetupUiEffect> =
        when (event) {
            is SetupUiEvent.PickDirectory -> pickDirectory(event.path)
            is SetupUiEvent.UpdatePath -> updatePath(event.path)
            SetupUiEvent.StartApp -> launchApp()
    }

    private fun pickDirectory(path: String): StateResult<SetupUiState, SetupUiEffect> =
        updatePath(if (path.endsWith(APP_NAME)) path else Path(path, APP_NAME).toString())

    private fun updatePath(path: String): StateResult<SetupUiState, SetupUiEffect> =
        StateResult.Changed(SetupUiState(path))

    private fun launchApp(): StateResult<SetupUiState, SetupUiEffect> {
        val path = uiState.value.path
        setupRepository.updatePath(path)
        logger.info { "User selected directory: $path - now launching application" }
        return StateResult.Unchanged(SetupUiEffect.Launch)
    }
}