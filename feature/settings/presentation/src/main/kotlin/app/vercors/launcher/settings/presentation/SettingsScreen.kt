package app.vercors.launcher.settings.presentation

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.mvi.MviContainer
import app.vercors.launcher.core.presentation.ui.AppScrollableColumn
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
                config = state.general,
                onIntent = onIntent
            )
            HomeSettingsSection(
                config = state.home,
                onIntent = onIntent
            )
        }
    }
}

