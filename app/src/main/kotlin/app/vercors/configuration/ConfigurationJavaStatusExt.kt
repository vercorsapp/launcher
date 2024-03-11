package app.vercors.configuration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.vercors.LocalPalette
import compose.icons.FeatherIcons
import compose.icons.feathericons.CheckCircle
import compose.icons.feathericons.Info
import compose.icons.feathericons.XCircle
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.checkingJava
import vercors.app.generated.resources.invalidJava
import vercors.app.generated.resources.validJava

val ConfigurationJavaStatus.icon: ImageVector
    @Stable get() = when (this) {
        ConfigurationJavaStatus.Checking -> FeatherIcons.Info
        ConfigurationJavaStatus.Valid -> FeatherIcons.CheckCircle
        ConfigurationJavaStatus.Invalid -> FeatherIcons.XCircle
    }

val ConfigurationJavaStatus.color: Color
    @Composable get() = when (this) {
        ConfigurationJavaStatus.Checking -> LocalPalette.current.blue
        ConfigurationJavaStatus.Valid -> LocalPalette.current.green
        ConfigurationJavaStatus.Invalid -> LocalPalette.current.red
    }

@Composable
fun ConfigurationJavaStatus.text(javaVersion: Int): String = stringResource(
    when (this) {
        ConfigurationJavaStatus.Checking -> Res.string.checkingJava
        ConfigurationJavaStatus.Valid -> Res.string.validJava
        ConfigurationJavaStatus.Invalid -> Res.string.invalidJava
    }, javaVersion
)