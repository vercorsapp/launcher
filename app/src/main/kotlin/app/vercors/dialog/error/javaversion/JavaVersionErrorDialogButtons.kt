package app.vercors.dialog.error.javaversion

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.vercors.UI
import app.vercors.common.AppButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.Settings
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.openSettings

@Composable
fun JavaVersionErrorDialogButtons(component: JavaVersionErrorDialogComponent) {
    AppButton(component::openSettings) {
        Icon(
            imageVector = FeatherIcons.Settings,
            contentDescription = null,
            modifier = Modifier.size(UI.mediumIconSize)
        )
        Text(
            text = stringResource(Res.string.openSettings),
            modifier = Modifier.padding(start = UI.mediumPadding)
        )
    }
}