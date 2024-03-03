package app.vercors.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface RootComponent {
    val child: Value<ChildSlot<*, RootChildComponent>>
}