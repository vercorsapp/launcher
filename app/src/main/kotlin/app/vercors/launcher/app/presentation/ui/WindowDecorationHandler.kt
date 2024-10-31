package app.vercors.launcher.app.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import app.vercors.launcher.core.generated.resources.app_title
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationScope.WindowDecorationHandler(
    state: WindowState,
    undecorated: Boolean,
    content: @Composable FrameWindowScope.() -> Unit
) {
    if (undecorated) {
        Window(
            state = state,
            title = stringResource(CoreString.app_title),
            undecorated = true,
            onCloseRequest = ::exitApplication,
            content = content
        )
    } else {
        Window(
            state = state,
            title = stringResource(CoreString.app_title),
            undecorated = false,
            onCloseRequest = ::exitApplication,
            content = content
        )
    }
}