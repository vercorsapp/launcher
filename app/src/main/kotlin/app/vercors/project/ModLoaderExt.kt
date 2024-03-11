package app.vercors.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.vectorResource
import vercors.app.generated.resources.*

val ModLoader.icon: ImageVector
    @Composable get() = when (this) {
        ModLoader.Forge -> vectorResource(Res.drawable.forge)
        ModLoader.NeoForge -> vectorResource(Res.drawable.neoforge)
        ModLoader.Fabric -> vectorResource(Res.drawable.fabric)
        ModLoader.Quilt -> vectorResource(Res.drawable.quilt)
    }