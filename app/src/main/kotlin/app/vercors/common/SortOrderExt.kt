package app.vercors.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.ascending
import vercors.app.generated.resources.descending

val SortOrder.title: String
    @Composable get() = stringResource(
        when (this) {
            SortOrder.Asc -> Res.string.ascending
            SortOrder.Desc -> Res.string.descending
        }
    )

val SortOrder.icon: ImageVector
    @Stable get() = when (this) {
        SortOrder.Asc -> FeatherIcons.ArrowUp
        SortOrder.Desc -> FeatherIcons.ArrowDown
    }