package app.vercors.launcher.instance.presentation.ui

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.viewmodel.MviContainer
import app.vercors.launcher.instance.presentation.viewmodel.InstanceListIntent
import app.vercors.launcher.instance.presentation.viewmodel.InstanceListUiState
import app.vercors.launcher.instance.presentation.viewmodel.InstanceListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InstanceListScreen() {
    MviContainer(koinViewModel<InstanceListViewModel>()) { uiState, onIntent ->
        InstanceListScreen(
            state = uiState,
            onIntent = onIntent
        )
    }
}

@Composable
fun InstanceListScreen(
    state: InstanceListUiState,
    onIntent: (InstanceListIntent) -> Unit
) {
    // TODO
}