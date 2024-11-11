package app.vercors.launcher.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.config.repository.ConfigUpdate
import app.vercors.launcher.core.presentation.viewmodel.MviViewModel
import app.vercors.launcher.core.presentation.viewmodel.StateResult
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val configRepository: ConfigRepository
) : MviViewModel<SettingsUiState, SettingsUiEvent, Nothing>(SettingsUiState.Loading) {
    override fun init() {
        viewModelScope.launch {
            configRepository.observeConfig().collect {
                onEvent(SettingsUiEvent.ConfigUpdated(it))
            }
        }
    }

    override fun reduce(state: SettingsUiState, event: SettingsUiEvent): StateResult<SettingsUiState, Nothing> =
        when (event) {
            is SettingsUiIntent.SelectAccent -> updateConfig(ConfigUpdate.General.Accent, event.value)
            is SettingsUiIntent.SelectTheme -> updateConfig(ConfigUpdate.General.Theme, event.value)
            is SettingsUiIntent.ToggleGradient -> updateConfig(ConfigUpdate.General.Gradient, event.value)
            is SettingsUiIntent.ToggleAnimations -> updateConfig(ConfigUpdate.General.Animations, event.value)
            is SettingsUiIntent.ToggleDecorated -> updateConfig(ConfigUpdate.General.Decorated, event.value)
            is SettingsUiIntent.SelectDefaultTab -> updateConfig(ConfigUpdate.General.DefaultTab, event.value)
            is SettingsUiEvent.UpdateSections -> updateConfig(ConfigUpdate.Home.Sections, event.value)
            is SettingsUiIntent.SelectProvider -> updateConfig(ConfigUpdate.Home.Provider, event.value)
            is SettingsUiEvent.ConfigUpdated -> StateResult.Changed(SettingsUiState.Loaded(config = event.config))
            is SettingsUiIntent.ToggleSection -> toggleSection(state, event.value)
        }

    private fun toggleSection(
        state: SettingsUiState,
        section: HomeSectionConfig
    ): StateResult<SettingsUiState, Nothing> =
        if (state is SettingsUiState.Loaded)
            updateConfig(
                ConfigUpdate.Home.Sections,
                if (section in state.config.home.sections) state.config.home.sections - section
                else (state.config.home.sections + section).sorted()
            )
        else StateResult.Unchanged()

    private fun <T> updateConfig(update: ConfigUpdate<T>, value: T): StateResult<SettingsUiState, Nothing> {
        viewModelScope.launch { configRepository.updateConfig(update, value) }
        return StateResult.Unchanged()
    }
}