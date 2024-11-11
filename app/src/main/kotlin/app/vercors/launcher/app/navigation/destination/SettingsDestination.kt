package app.vercors.launcher.app.navigation.destination

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.viewmodel.ViewModelContainer
import app.vercors.launcher.settings.presentation.ui.SettingsScreen
import app.vercors.launcher.settings.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsDestination() {
    ViewModelContainer(koinViewModel<SettingsViewModel>()) { uiState, onEvent ->
        SettingsScreen(
            state = uiState,
            onIntent = onEvent
        )
    }
}