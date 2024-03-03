package app.vercors.navigation

import com.arkivanov.decompose.value.Value

interface NavigationComponent {
    val children: Value<NavigationChildren<*, NavigationChildComponent>>

    fun navigate(event: NavigationEvent)
}
