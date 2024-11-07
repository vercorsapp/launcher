package app.vercors.launcher.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.config.repository.*
import app.vercors.launcher.settings.presentation.action.SettingsAction
import app.vercors.launcher.settings.presentation.state.SettingsUiState
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

private val logger = KotlinLogging.logger {}

@KoinViewModel
class SettingsViewModel(
    private val configRepository: ConfigRepository
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = configRepository.observeConfig()
        .map { SettingsUiState.Loaded(config = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), SettingsUiState.Loading)

    fun onAction(action: SettingsAction) {
        logger.debug { "Action triggered in SettingsViewModel: $action" }
        when (action) {
            is SettingsAction.SelectAccent -> updateConfig(AccentConfigUpdate, action.value)
            is SettingsAction.SelectTheme -> updateConfig(ThemeConfigUpdate, action.value)
            is SettingsAction.ToggleAnimations -> updateConfig(AnimationsConfigUpdate, action.value)
            is SettingsAction.ToggleDecorated -> updateConfig(DecoratedConfigUpdate, action.value)
            is SettingsAction.SelectDefaultTab -> updateConfig(DefaultTabConfigUpdate, action.value)
        }
    }

    private fun <T> updateConfig(update: ConfigUpdate<T>, value: T) =
        viewModelScope.launch { configRepository.updateConfig(update, value) }
}