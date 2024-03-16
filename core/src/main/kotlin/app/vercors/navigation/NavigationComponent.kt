package app.vercors.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface NavigationComponent : NavigationEventHandler {
    val childState: StateFlow<ChildStack<*, NavigationChildComponent>>
}
