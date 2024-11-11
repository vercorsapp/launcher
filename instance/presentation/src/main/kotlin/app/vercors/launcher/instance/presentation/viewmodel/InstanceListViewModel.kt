package app.vercors.launcher.instance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.vercors.launcher.instance.presentation.action.InstanceListAction
import app.vercors.launcher.instance.presentation.state.InstanceListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class InstanceListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(InstanceListUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: InstanceListAction) {
        // TODO
    }
}