package app.vercors.launcher.core.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.vercors.launcher.core.presentation.ui.ObserveAsEvents

@Composable
fun <State, Event, Intent : Event, Effect> ViewModelContainer(
    viewModel: MviViewModel<State, Event, Effect>,
    onEffect: (Effect) -> Unit,
    minActiveState: Lifecycle.State? = Lifecycle.State.STARTED,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    ObserveAsEvents(viewModel.uiEffects, onEvent = onEffect)
    ViewModelContainer(
        viewModel = viewModel,
        minActiveState = minActiveState,
        content = content
    )
}

@Composable
fun <State, Event, Intent : Event, Effect> ViewModelContainer(
    viewModel: MviViewModel<State, Event, Effect>,
    minActiveState: Lifecycle.State? = Lifecycle.State.STARTED,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    val uiState by viewModel.uiState.run {
        if (minActiveState != null) collectAsStateWithLifecycle(minActiveState = minActiveState)
        else collectAsState()
    }
    content(uiState, viewModel::onEvent)
}