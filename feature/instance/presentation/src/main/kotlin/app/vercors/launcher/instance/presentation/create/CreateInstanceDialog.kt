package app.vercors.launcher.instance.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.generated.resources.plus
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.dialog.AppDialogCloseButton
import app.vercors.launcher.core.presentation.dialog.AppDialogContainer
import app.vercors.launcher.core.presentation.mvi.MviContainer
import app.vercors.launcher.core.presentation.ui.AppIconButton
import app.vercors.launcher.instance.generated.resources.create
import app.vercors.launcher.instance.generated.resources.create_new_instance
import app.vercors.launcher.instance.presentation.InstanceString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateInstanceDialog(
    onClose: () -> Unit,
) {
    MviContainer(
        viewModel = koinViewModel<CreateInstanceViewModel>(),
        onEffect = {
            when (it) {
                CreateInstanceUiEffect.CloseDialog -> onClose()
            }
        }
    ) { state, onIntent ->
        CreateInstanceDialog(
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
internal fun CreateInstanceDialog(
    state: CreateInstanceUiState,
    onIntent: (CreateInstanceUiIntent) -> Unit,
) {
    AppDialogContainer(
        title = stringResource(InstanceString.create_new_instance),
        buttons = {
            AppIconButton(
                onClick = { onIntent(CreateInstanceUiIntent.CreateInstance) },
                icon = vectorResource(CoreDrawable.plus),
                text = stringResource(InstanceString.create)
            )
            AppDialogCloseButton { onIntent(CreateInstanceUiIntent.CloseDialog) }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    label = { Text("Name") },
                    value = state.instanceName,
                    onValueChange = { onIntent(CreateInstanceUiIntent.UpdateInstanceName(it)) }
                )
            }
        }
    }
}