package app.vercors.launcher.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vercors.launcher.app.presentation.action.AppAction
import app.vercors.launcher.app.presentation.state.AppDialog
import app.vercors.launcher.app.presentation.state.AppUiState
import app.vercors.launcher.core.config.repository.ConfigRepository
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Single

@Single
class AppViewModel(
    configRepository: ConfigRepository
) : ViewModel() {
    private val _dialogState = MutableStateFlow<AppDialog?>(null)
    val uiState = combine(
        _dialogState.asStateFlow(),
        configRepository.configState
    ) { dialog, config ->
        AppUiState(
            currentDialog = dialog,
            undecorated = !config.general.decorated
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = AppUiState(
            undecorated = true,
            currentDialog = null
        )
    )

    fun onAction(action: AppAction) {
        when (action) {
            AppAction.CloseDialog -> _dialogState.update { null }
            is AppAction.OpenDialog -> _dialogState.update { action.dialog }
        }
    }
}