package app.vercors.launcher.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.vercors.launcher.app.presentation.action.AppAction
import app.vercors.launcher.app.presentation.action.MenuBarAction
import app.vercors.launcher.app.presentation.state.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AppAction) {
        when (action) {
            is MenuBarAction.SearchQueryChange -> _uiState.update { it.copy(searchQuery = action.query) }
            else -> Unit
        }
    }
}