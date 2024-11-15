package app.vercors.launcher.home.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.ui.AppScrollableLazyColumn
import app.vercors.launcher.core.presentation.viewmodel.MviContainer
import app.vercors.launcher.home.presentation.viewmodel.HomeUiEffect
import app.vercors.launcher.home.presentation.viewmodel.HomeUiIntent
import app.vercors.launcher.home.presentation.viewmodel.HomeUiState
import app.vercors.launcher.home.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onEffect: (HomeUiEffect) -> Unit,
) {
    MviContainer(
        viewModel = koinViewModel<HomeViewModel>(),
        onEffect = onEffect
    ) { state, onIntent ->
        HomeScreen(
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeUiIntent) -> Unit
) {
    AppScrollableLazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.sections) { section ->
            HomeSection(
                section = section,
                onIntent = onIntent
            )
        }
    }
}