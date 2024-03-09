package app.vercors.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.LocalPalette
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertOctagon
import compose.icons.feathericons.AlertTriangle
import compose.icons.feathericons.Info
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.instanceNotFound
import vercors.app.generated.resources.notificationError

val NotificationData.icon: ImageVector
    @Stable get() = when (level) {
        NotificationLevel.INFO -> FeatherIcons.Info
        NotificationLevel.WARN -> FeatherIcons.AlertTriangle
        NotificationLevel.ERROR -> FeatherIcons.AlertOctagon
    }

val NotificationData.color: Color
    @Composable get() = when (level) {
        NotificationLevel.INFO -> LocalPalette.current.blue
        NotificationLevel.WARN -> LocalPalette.current.yellow
        NotificationLevel.ERROR -> LocalPalette.current.red
    }

@OptIn(ExperimentalResourceApi::class)
val NotificationData.actualText: String
    @Composable get() = when (text) {
        is NotificationText.Literal -> (text as NotificationText.Literal).text
        NotificationText.Template.InstanceNotFound -> stringResource(Res.string.instanceNotFound, *args)
        NotificationText.Template.Error -> stringResource(Res.string.notificationError, *args)

    }