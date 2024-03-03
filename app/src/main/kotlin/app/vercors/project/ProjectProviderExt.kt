package app.vercors.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.curseforge
import vercors.app.generated.resources.modrinth

@OptIn(ExperimentalResourceApi::class)
val ProjectProviderType.icon: ImageVector
    @Composable get() = vectorResource(
        when (this) {
            ProjectProviderType.Modrinth -> Res.drawable.modrinth
            ProjectProviderType.Curseforge -> Res.drawable.curseforge
        }
    )