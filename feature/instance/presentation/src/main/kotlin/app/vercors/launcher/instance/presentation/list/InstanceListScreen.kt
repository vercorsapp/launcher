package app.vercors.launcher.instance.presentation.list

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.mvi.MviContainer
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