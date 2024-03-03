package app.vercors.root

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.APP_NAME
import app.vercors.UI
import app.vercors.common.AppLabeledCheckbox
import app.vercors.common.IconTextButton
import app.vercors.root.setup.SetupComponent
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import compose.icons.FeatherIcons
import compose.icons.feathericons.Folder
import compose.icons.feathericons.Play
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SetupContent(component: SetupComponent) {
    val uiState by component.uiState.collectAsState()

    AppWindowContent(
        config = uiState.config
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
            modifier = Modifier.padding(UI.largePadding)
        ) {
            Text(stringResource(Res.string.welcome, APP_NAME), style = MaterialTheme.typography.h5)
            Text(stringResource(Res.string.selectDataDir))
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.path,
                    onValueChange = component::updatePath,
                    modifier = Modifier.width(600.dp)
                )
                IconTextButton(
                    onClick = component::openDirectoryPicker,
                    imageVector = FeatherIcons.Folder,
                    text = stringResource(Res.string.browse),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = UI.mediumPadding)
            ) {
                AppLabeledCheckbox(
                    checked = uiState.showTutorial,
                    onCheckedChange = component::updateShowTutorial,
                    text = stringResource(Res.string.showTutorial)
                )
            }
            IconTextButton(
                onClick = component::launch,
                imageVector = FeatherIcons.Play,
                text = stringResource(Res.string.launch)
            )
        }
    }

    DirectoryPicker(
        show = uiState.showDirectoryPicker,
        initialDirectory = uiState.path
    ) { path ->
        path?.let { component.updatePath(it) }
        component.closeDirectoryPicker()
    }
}