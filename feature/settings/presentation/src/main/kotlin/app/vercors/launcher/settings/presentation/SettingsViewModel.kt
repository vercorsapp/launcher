package app.vercors.launcher.settings.presentation

import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.model.HomeSectionConfig
import app.vercors.launcher.core.config.repository.ConfigRepository
import app.vercors.launcher.core.config.repository.ConfigUpdate
import app.vercors.launcher.core.presentation.mvi.MviViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val configRepository: ConfigRepository
) : MviViewModel<SettingsUiState, SettingsUiIntent, Nothing>(SettingsUiState.Loading) {
    override fun onStart() {
        super.onStart()
        collectInScope(configRepository.observeConfig()) {
            onIntent(ConfigUpdated(it))
        }
    }

    override fun SettingsUiState.reduce(intent: SettingsUiIntent) =
        when (intent) {
            is SettingsUiIntent.SelectAccent -> updateConfig(ConfigUpdate.General.Accent, intent.value)
            is SettingsUiIntent.SelectTheme -> updateConfig(ConfigUpdate.General.Theme, intent.value)
            is SettingsUiIntent.ToggleGradient -> updateConfig(ConfigUpdate.General.Gradient, intent.value)
            is SettingsUiIntent.ToggleAnimations -> updateConfig(ConfigUpdate.General.Animations, intent.value)
            is SettingsUiIntent.ToggleDecorated -> updateConfig(ConfigUpdate.General.Decorated, intent.value)
            is SettingsUiIntent.SelectDefaultTab -> updateConfig(ConfigUpdate.General.DefaultTab, intent.value)
            is SettingsUiIntent.ToggleSection -> toggleSection(intent.value)
            is SettingsUiIntent.SelectProvider -> updateConfig(ConfigUpdate.Home.Provider, intent.value)
            is ConfigUpdated -> SettingsUiState.Loaded(
                general = GeneralConfigUi(intent.config.general),
                home = HomeConfigUi(intent.config.home),
            )
        }

    private fun SettingsUiState.toggleSection(section: HomeSectionConfig): SettingsUiState =
        if (this is SettingsUiState.Loaded)
            updateConfig(
                ConfigUpdate.Home.Sections,
                if (section in home.sections) home.sections - section
                else (home.sections + section).sorted()
            )
        else this

    private fun <T> SettingsUiState.updateConfig(update: ConfigUpdate<T>, value: T): SettingsUiState {
        runInScope { configRepository.updateConfig(update, value) }
        return this
    }

    @JvmInline
    private value class ConfigUpdated(val config: AppConfig) : SettingsUiIntent
}