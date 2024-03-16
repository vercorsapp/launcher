package app.vercors.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.WindowState
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent

@Composable
fun RootContent(component: RootComponent, windowState: WindowState, onClose: () -> Unit) {
    val childState by component.childState.collectAsState()

    when (val childComponent = childState.child?.instance) {
        is ErrorComponent -> AppWindow(
                onClose = onClose,
                windowState = windowState
        ) {
            LaunchedEffect(Unit) {
                window.setSize(600, 400)
            }
            ErrorContent(childComponent)
        }

        is MainComponent -> MainContent(childComponent, windowState, onClose)
        is SetupComponent -> AppWindow(
                onClose = onClose,
                windowState = windowState
        ) {
            LaunchedEffect(Unit) {
                window.setSize(800, 320)
            }
            SetupContent(childComponent)
        }
    }
}
