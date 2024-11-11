package app.vercors.launcher.app.navigation.destination

import androidx.compose.runtime.Composable
import app.vercors.launcher.core.presentation.viewmodel.ViewModelContainer
import app.vercors.launcher.home.presentation.ui.HomeScreen
import app.vercors.launcher.home.presentation.viewmodel.HomeUiEffect
import app.vercors.launcher.home.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeDestination(onEffect: (HomeUiEffect) -> Unit) {
    ViewModelContainer(
        viewModel = koinViewModel<HomeViewModel>(),
        onEffect = onEffect
    ) { state, onIntent ->
        HomeScreen(
            state = state,
            onIntent = onIntent
        )
    }
}