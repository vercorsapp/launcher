package app.vercors.launcher.app.state

import androidx.compose.runtime.Immutable
import app.vercors.launcher.core.config.model.TabConfig

@Immutable
data class AppUiState(
    val generalConfig: GeneralConfigState = GeneralConfigState.Loading,
    val currentDialog: AppDialog? = null,
)

sealed interface GeneralConfigState {
    data object Loading : GeneralConfigState
    data class Loaded(
        val theme: String,
        val accent: String,
        val decorated: Boolean,
        val animations: Boolean,
        val defaultTab: TabConfig
    ) : GeneralConfigState
}