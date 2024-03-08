package app.vercors.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface NavigationComponent : NavigationEventHandler {
    val children: Value<ChildStack<*, NavigationChildComponent>>
}
