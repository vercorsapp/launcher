package app.vercors.launcher.core.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.vercors.launcher.core.presentation.ui.ObserveAsEvents

@Composable
fun <State, Event, Intent : Event, Effect> ViewModelContainer(
    viewModel: MviViewModel<State, Event, Effect>,
    onEffect: (Effect) -> Unit,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.uiEffects, onEvent = onEffect)
    content(uiState, viewModel::onEvent)
}

@Composable
fun <State, Event, Intent : Event, Effect> ViewModelContainer(
    viewModel: MviViewModel<State, Event, Effect>,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    content(uiState, viewModel::onEvent)
}