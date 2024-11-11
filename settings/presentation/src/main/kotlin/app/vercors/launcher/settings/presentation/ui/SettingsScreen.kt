package app.vercors.launcher.settings.presentation.ui

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.ui.AppScrollableColumn
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiIntent
import app.vercors.launcher.settings.presentation.viewmodel.SettingsUiState

@Composable
fun SettingsScreen(
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

