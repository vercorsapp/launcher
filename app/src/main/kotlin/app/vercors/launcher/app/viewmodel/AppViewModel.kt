package app.vercors.launcher.app.viewmodel

import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.presentation.viewmodel.MviViewModel
import app.vercors.launcher.core.presentation.viewmodel.StateResult
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class AppViewModel(
    private val configRepository: ConfigRepository
) : MviViewModel<AppUiState, AppUiEvent, Nothing>(AppUiState()) {
    override fun init() {
        viewModelScope.launch {
            configRepository.observeConfig().collect {
                onEvent(AppUiEvent.ConfigUpdated(it))
            }
        }
    }

    override fun reduce(state: AppUiState, event: AppUiEvent): StateResult<AppUiState, Nothing> = when (event) {
        AppUiIntent.CloseDialog -> StateResult.Changed(state.copy(currentDialog = null))
        is AppUiIntent.OpenDialog -> StateResult.Changed(state.copy(currentDialog = event.dialog))
        is AppUiEvent.ConfigUpdated -> StateResult.Changed(
            state.copy(
                generalConfig = updateConfigState(
                    state.generalConfig,
                    event.config.general
                )
            )
        )
    }

    private fun updateConfigState(
        previousState: GeneralConfigState,
        config: GeneralConfig
    ): GeneralConfigState = when (previousState) {
        is GeneralConfigState.Loaded -> previousState.copy(
            theme = config.theme,
            accent = config.accent,
            gradient = config.gradient,
            animations = config.animations
        )

        GeneralConfigState.Loading -> GeneralConfigState.Loaded(
            theme = config.theme,
            accent = config.accent,
            gradient = config.gradient,
            decorated = config.decorated,
            animations = config.animations,
            defaultTab = config.defaultTab
        )
    }
}