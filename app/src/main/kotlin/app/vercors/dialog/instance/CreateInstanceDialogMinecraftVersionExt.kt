package app.vercors.dialog.instance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Star
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.latestRelease
import vercors.app.generated.resources.latestSnapshot

val CreateInstanceDialogMinecraftVersion.text: String
    @Composable get() =
        if (isLatestRelease) stringResource(Res.string.latestRelease, data.id)
        else if (isLatestSnapshot) stringResource(Res.string.latestSnapshot, data.id)
        else data.id

val CreateInstanceDialogMinecraftVersion.icon: ImageVector?
    @Composable get() =
        if (isLatestRelease) FeatherIcons.Star
        else if (isLatestSnapshot) FeatherIcons.Clock
        else null