package app.vercors.launcher.app.navigation.destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.vercors.launcher.instance.presentation.ui.InstanceListScreen
import app.vercors.launcher.instance.presentation.viewmodel.InstanceListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InstanceListDestination() {
    val viewModel = koinViewModel<InstanceListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InstanceListScreen(
        state = uiState,
        onAction = viewModel::onAction
    )
}