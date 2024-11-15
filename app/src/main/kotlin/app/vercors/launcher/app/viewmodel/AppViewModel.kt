package app.vercors.launcher.app.viewmodel

import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.model.GeneralConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.presentation.viewmodel.MviViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class AppViewModel(
    private val configRepository: ConfigRepository
) : MviViewModel<AppUiState, AppUiIntent, Nothing>(AppUiState()) {
    override fun onStart() {
        super.onStart()
        viewModelScope.launch {
            configRepository.observeConfig().collect {
                onIntent(ConfigUpdated(it))
            }
        }
    }

    override fun AppUiState.reduce(intent: AppUiIntent) = when (intent) {
        AppUiIntent.CloseDialog -> copy(currentDialog = null)
        is AppUiIntent.OpenDialog -> copy(currentDialog = intent.dialog)
        is ConfigUpdated -> copy(generalConfig = generalConfig.updateConfigState(intent.config.general))
    }

    private fun GeneralConfigState.updateConfigState(config: GeneralConfig): GeneralConfigState = when (this) {
        is GeneralConfigState.Loaded -> copy(
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

    @JvmInline
    private value class ConfigUpdated(val config: AppConfig) : AppUiIntent
}