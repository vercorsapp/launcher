package app.vercors.launcher.home.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.launcher.core.presentation.ui.AppScrollableLazyColumn
import app.vercors.launcher.home.presentation.viewmodel.HomeUiIntent
import app.vercors.launcher.home.presentation.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeUiIntent) -> Unit
) {
    AppScrollableLazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.sections) { section ->
            HomeSection(
                section = section,
                onAction = onIntent
            )
        }
    }
}