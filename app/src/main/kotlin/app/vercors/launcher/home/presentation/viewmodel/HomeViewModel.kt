package app.vercors.launcher.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.vercors.launcher.home.presentation.action.HomeAction
import app.vercors.launcher.home.presentation.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: HomeAction): Unit = when (action) {
        is HomeAction.InstallProject -> TODO()
        is HomeAction.LaunchInstance -> TODO()
        is HomeAction.ShowInstance -> TODO()
        is HomeAction.ShowProject -> TODO()
    }
}