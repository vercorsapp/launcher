package app.vercors.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import app.vercors.APP_NAME

@Composable
fun AppWindow(
    onClose: () -> Unit,
    windowState: WindowState = rememberWindowState(),
    undecorated: Boolean = false,
    content: @Composable WindowScope.() -> Unit
) {
    Window(
        state = windowState,
        onCloseRequest = onClose,
        undecorated = undecorated,
        title = APP_NAME,
        content = content
    )
}