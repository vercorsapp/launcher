package app.vercors.instance

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.gameVersion
import vercors.app.generated.resources.loader
import vercors.app.generated.resources.none

val InstanceGroupBy.title: String
    @Composable get() = stringResource(
        when (this) {
            InstanceGroupBy.None -> Res.string.none
            InstanceGroupBy.GameVersion -> Res.string.gameVersion
            InstanceGroupBy.Loader -> Res.string.loader
        }
    )