package app.vercors.launcher.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vercors.launcher.app.presentation.action.AppAction
import app.vercors.launcher.app.presentation.state.AppUiState
import app.vercors.launcher.app.presentation.state.GeneralConfigState
import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {}

@Single
class AppViewModel(
    configRepository: ConfigRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            configRepository.observeConfig().collect { config ->
                _uiState.update {
                    it.copy(generalConfig = updateConfigState(it.generalConfig, config.general))
                }
            }
        }
    }

    fun onAction(action: AppAction) {
        logger.debug { "Action triggered in AppViewModel: $action" }
        when (action) {
            AppAction.CloseDialog -> _uiState.update { it.copy(currentDialog = null) }
            is AppAction.OpenDialog -> _uiState.update { it.copy(currentDialog = action.dialog) }
        }
    }

    private fun updateConfigState(
        previousState: GeneralConfigState,
        config: GeneralConfig
    ): GeneralConfigState = when (previousState) {
        is GeneralConfigState.Loaded -> previousState.copy(
            theme = config.theme,
            accent = config.accent
        )

        GeneralConfigState.Loading -> GeneralConfigState.Loaded(
            theme = config.theme,
            accent = config.accent,
            defaultTab = config.defaultTab,
            decorated = config.decorated
        )
    }
}