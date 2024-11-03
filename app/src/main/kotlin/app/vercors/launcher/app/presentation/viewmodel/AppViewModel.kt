package app.vercors.launcher.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.vercors.launcher.app.presentation.action.AppAction
import app.vercors.launcher.app.presentation.state.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AppAction) {
        when (action) {
            AppAction.CloseDialog -> _uiState.update { it.copy(currentDialog = null) }
            is AppAction.OpenDialog -> _uiState.update { it.copy(currentDialog = action.dialog) }
        }
    }
}