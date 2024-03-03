package app.vercors.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import app.vercors.APP_NAME
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AppWindow(
    lifecycle: LifecycleRegistry,
    onClose: () -> Unit,
    windowState: WindowState = rememberWindowState(),
    undecorated: Boolean = false,
    content: @Composable WindowScope.() -> Unit
) {
    LifecycleController(lifecycle, windowState)

    Window(
        state = windowState,
        onCloseRequest = onClose,
        undecorated = undecorated,
        title = APP_NAME,
        content = content
    )
}