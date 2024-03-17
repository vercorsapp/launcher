package app.vercors.dialog.error

import androidx.compose.runtime.Composable
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

val ErrorDialogComponent.title: String
    @Composable get() = when (this) {
        is LaunchErrorDialogComponent -> stringResource(Res.string.launchErrorTitle)
        is JavaVersionErrorDialogComponent -> stringResource(Res.string.javaVersionErrorTitle)
        else -> ""
    }

val ErrorDialogComponent.message: String
    @Composable get() = when (this) {
        is LaunchErrorDialogComponent -> stringResource(Res.string.launchErrorMessage)
        is JavaVersionErrorDialogComponent -> stringResource(Res.string.javaVersionErrorMessage, javaVersion)
        else -> ""
    }