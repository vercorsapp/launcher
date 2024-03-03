package app.vercors.navigation

import app.vercors.common.Refreshable
import com.arkivanov.decompose.Child

class NavigationChildren<out C : Any, out T : Any>(
    val items: List<Child.Created<C, T>>,
    val index: Int
) {
    val active: Child.Created<C, T> = items[index]
    val hasPreviousScreen = index > 0
    val hasNextScreen = index < items.size - 1
    val canRefreshScreen = active.instance is Refreshable
}