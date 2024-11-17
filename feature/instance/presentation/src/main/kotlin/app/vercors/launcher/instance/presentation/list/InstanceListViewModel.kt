package app.vercors.launcher.instance.presentation.list

import app.vercors.launcher.core.presentation.mvi.MviViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class InstanceListViewModel : MviViewModel<InstanceListUiState, InstanceListIntent, Nothing>(InstanceListUiState()) {
    override fun InstanceListUiState.reduce(intent: InstanceListIntent): InstanceListUiState {
        return this
    }
}