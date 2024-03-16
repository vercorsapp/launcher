package app.vercors.root

import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {
    val childState: StateFlow<ChildSlot<*, RootChildComponent>>
}