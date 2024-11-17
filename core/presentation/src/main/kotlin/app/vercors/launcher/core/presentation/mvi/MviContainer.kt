package app.vercors.launcher.core.presentation.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.vercors.launcher.core.presentation.ui.ObserveAsEvents

@Composable
fun <State, Intent, Effect> MviContainer(
    viewModel: MviViewModel<State, Intent, Effect>,
    onEffect: (Effect) -> Unit,
    minActiveState: Lifecycle.State? = Lifecycle.State.STARTED,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    ObserveAsEvents(viewModel.effects, onEvent = onEffect)
    MviContainer(
        viewModel = viewModel,
        minActiveState = minActiveState,
        content = content
    )
}

@Composable
fun <State, Intent, Effect> MviContainer(
    viewModel: MviViewModel<State, Intent, Effect>,
    minActiveState: Lifecycle.State? = Lifecycle.State.STARTED,
    content: @Composable (state: State, onIntent: (Intent) -> Unit) -> Unit
) {
    val uiState by viewModel.state.run {
        if (minActiveState != null) collectAsStateWithLifecycle(minActiveState = minActiveState)
        else collectAsState()
    }
    content(uiState, viewModel::onIntent)
}