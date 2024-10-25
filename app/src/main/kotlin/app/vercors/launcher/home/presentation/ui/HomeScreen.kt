package app.vercors.launcher.home.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.vercors.launcher.home.presentation.action.HomeAction
import app.vercors.launcher.home.presentation.state.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    Text("Welcome home!")
}