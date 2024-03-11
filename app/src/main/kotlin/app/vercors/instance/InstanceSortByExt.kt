package app.vercors.instance

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

val InstanceSortBy.title: String
    @Composable get() = stringResource(
        when (this) {
            InstanceSortBy.LastPlayed -> Res.string.lastPlayed
            InstanceSortBy.DateModified -> Res.string.dateModified
            InstanceSortBy.DateCreated -> Res.string.dateCreated
            InstanceSortBy.GameVersion -> Res.string.gameVersion
            InstanceSortBy.Name -> Res.string.name
        }
    )