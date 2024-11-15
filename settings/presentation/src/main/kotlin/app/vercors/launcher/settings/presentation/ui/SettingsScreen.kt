package app.vercors.launcher.settings.presentation.ui

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.ui.AppScrollableColumn
import app.vercors.launcher.core.presentation.viewmodel.MviContainer
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiIntent
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiState
import app.vercors.launcher.settings.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen() {
    MviContainer(koinViewModel<SettingsViewModel>()) { uiState, onIntent ->
        SettingsScreen(
            state = uiState,
            onIntent = onIntent
        )
    }
}

@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    onIntent: (SettingsUiIntent) -> Unit,
) {
    if (state is SettingsUiState.Loaded) {
        AppScrollableColumn {
            GeneralSettingsSection(
                state = state,
                onIntent = onIntent
            )
            HomeSettingsSection(
                state = state,
                onIntent = onIntent
            )
        }
    }
}

