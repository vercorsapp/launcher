package app.vercors.launcher.setup.presentation

import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.core.presentation.mvi.MviViewModel
import app.vercors.launcher.setup.domain.SetupRepository
import kotlinx.io.files.Path
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SetupViewModel(
    private val setupRepository: SetupRepository
) : MviViewModel<SetupUiState, SetupUiEvent, SetupUiEffect>(SetupUiState(setupRepository.defaultPath)) {
    override fun SetupUiState.reduce(intent: SetupUiEvent): SetupUiState =
        when (intent) {
            is SetupUiEvent.PickDirectory -> pickDirectory(intent.path)
            is SetupUiEvent.UpdatePath -> updatePath(intent.path)
            SetupUiEvent.StartApp -> launchApp()
        }

    private fun SetupUiState.pickDirectory(path: String): SetupUiState =
        updatePath(if (path.endsWith(APP_NAME)) path else Path(path, APP_NAME).toString())

    private fun SetupUiState.updatePath(path: String): SetupUiState = SetupUiState(path)

    private fun SetupUiState.launchApp(): SetupUiState {
        val path = state.value.path
        setupRepository.updatePath(path)
        logger.info { "User selected directory: $path - now launching application" }
        return withEffect(SetupUiEffect.Launch)
    }
}