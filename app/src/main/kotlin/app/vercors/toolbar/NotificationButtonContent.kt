package app.vercors.toolbar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bell
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.notifications

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NotificationButtonContent(interactionSource: MutableInteractionSource, onClick: () -> Unit) {
    ToolbarButtonContent(
        icon = FeatherIcons.Bell,
        name = stringResource(Res.string.notifications),
        interactionSource = interactionSource,
        onClick = onClick,
    )
}