package app.vercors.launcher.app.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.app.AppString
import app.vercors.launcher.app.generated.resources.start_app
import app.vercors.launcher.app.generated.resources.welcome
import app.vercors.launcher.app.generated.resources.welcome_path
import app.vercors.launcher.core.generated.resources.folder
import app.vercors.launcher.core.generated.resources.open_folder
import app.vercors.launcher.core.generated.resources.play
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
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
            text = stringResource(AppString.welcome),
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.height(20.dp))
        Text(text = stringResource(AppString.welcome_path))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.path,
                onValueChange = { onAction(SetupAction.UpdatePath(it)) },
                modifier = Modifier.width(450.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = filePicker::launch
            ) {
                Icon(
                    imageVector = vectorResource(CoreDrawable.folder),
                    contentDescription = null
                )
                Text(
                    text = stringResource(CoreString.open_folder),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { onAction(SetupAction.StartApp) },
        ) {
            Icon(
                imageVector = vectorResource(CoreDrawable.play),
                contentDescription = null
            )
            Text(
                text = stringResource(AppString.start_app),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}