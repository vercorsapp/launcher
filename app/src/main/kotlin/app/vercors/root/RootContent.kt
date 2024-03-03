package app.vercors.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@Composable
fun RootContent(component: RootComponent, lifecycle: LifecycleRegistry, onClose: () -> Unit) {
    val state by component.child.subscribeAsState()
    when (val childComponent = state.child?.instance) {
        is ErrorComponent -> AppWindow(
            lifecycle = lifecycle,
            onClose = onClose,
            windowState = rememberWindowState(size = DpSize(600.dp, 400.dp))
        ) {
            ErrorContent(childComponent)
        }

        is MainComponent -> MainContent(childComponent, lifecycle, onClose)
        is SetupComponent -> AppWindow(
            lifecycle = lifecycle,
            onClose = onClose,
            windowState = rememberWindowState(size = DpSize(800.dp, 320.dp))
        ) {
            SetupContent(childComponent)
        }
    }
}
