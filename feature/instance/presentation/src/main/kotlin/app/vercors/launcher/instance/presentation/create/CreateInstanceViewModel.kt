package app.vercors.launcher.instance.presentation.create

import app.vercors.launcher.core.presentation.mvi.MviViewModel
import kotlinx.coroutines.flow.SharingStarted
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreateInstanceViewModel : MviViewModel<CreateInstanceUiState, CreateInstanceUiIntent, CreateInstanceUiEffect>(
    CreateInstanceUiState(),
    SharingStarted.WhileSubscribed()
) {
    override fun onStart() {
        updateState(CreateInstanceUiState())
    }

    override fun CreateInstanceUiState.reduce(intent: CreateInstanceUiIntent): CreateInstanceUiState = when (intent) {
        is CreateInstanceUiIntent.UpdateInstanceName -> copy(instanceName = intent.value)
        CreateInstanceUiIntent.CreateInstance -> this
        CreateInstanceUiIntent.CloseDialog -> withEffect(CreateInstanceUiEffect.CloseDialog)
    }
}
