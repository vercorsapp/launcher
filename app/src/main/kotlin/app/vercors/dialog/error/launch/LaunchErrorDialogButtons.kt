package app.vercors.dialog.error.launch

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.UI
import app.vercors.common.AppButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.File
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.openLogs

@Composable
fun LaunchErrorDialogButtons(component: LaunchErrorDialogComponent) {
    AppButton(component::openLogFolder) {
        Icon(
            imageVector = FeatherIcons.File,
            contentDescription = null,
            modifier = Modifier.size(UI.mediumIconSize)
        )
        Text(
            text = stringResource(Res.string.openLogs),
            modifier = Modifier.padding(start = UI.mediumPadding)
        )
    }
}