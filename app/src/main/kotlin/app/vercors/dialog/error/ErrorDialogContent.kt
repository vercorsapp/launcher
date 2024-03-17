package app.vercors.dialog.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.common.AppTextButton
import app.vercors.common.appAnimateContentSize
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogButtons
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogButtons
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
import compose.icons.FeatherIcons
import compose.icons.feathericons.X
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.close

@Composable
fun ErrorDialogContent(component: ErrorDialogComponent) {
    Column(
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding).width(500.dp).appAnimateContentSize()
    ) {
        Text(component.title, style = MaterialTheme.typography.h5)
        Text(component.message)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.End)
        ) {
            when (component) {
                is JavaVersionErrorDialogComponent -> JavaVersionErrorDialogButtons(component)
                is LaunchErrorDialogComponent -> LaunchErrorDialogButtons(component)
            }
            AppTextButton(component::close) {
                Icon(
                    imageVector = FeatherIcons.X,
                    contentDescription = null,
                    modifier = Modifier.size(UI.mediumIconSize)
                )
                Text(
                    text = stringResource(Res.string.close),
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }
        }
    }
}