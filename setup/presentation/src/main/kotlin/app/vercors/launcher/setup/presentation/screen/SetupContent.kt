package app.vercors.launcher.setup.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.generated.resources.folder
import app.vercors.launcher.core.generated.resources.open_folder
import app.vercors.launcher.core.generated.resources.play
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.ui.AppIconButton
import app.vercors.launcher.setup.generated.resources.start_app
import app.vercors.launcher.setup.generated.resources.welcome
import app.vercors.launcher.setup.generated.resources.welcome_path
import app.vercors.launcher.setup.presentation.SetupString
import app.vercors.launcher.setup.presentation.action.SetupAction
import app.vercors.launcher.setup.presentation.state.SetupUiState
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SetupContent(
    uiState: SetupUiState,
    onAction: (SetupAction) -> Unit
) {
    val filePicker = rememberDirectoryPickerLauncher(initialDirectory = uiState.parentPath) { directory ->
        directory?.path?.let { onAction(SetupAction.PickDirectory(it)) }
    }

    Column(
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(SetupString.welcome),
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.height(20.dp))
        Text(text = stringResource(SetupString.welcome_path))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.path,
                onValueChange = { onAction(SetupAction.UpdatePath(it)) },
                modifier = Modifier.width(450.dp)
            )
            AppIconButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = filePicker::launch,
                icon = vectorResource(CoreDrawable.folder),
                text = stringResource(CoreString.open_folder)
            )
        }
        Spacer(Modifier.height(20.dp))
        AppIconButton(
            onClick = { onAction(SetupAction.StartApp) },
            icon = vectorResource(CoreDrawable.play),
            text = stringResource(SetupString.start_app)
        )
    }
}