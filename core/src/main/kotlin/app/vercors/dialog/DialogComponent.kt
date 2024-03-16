package app.vercors.dialog

import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow

interface DialogComponent : DialogEventHandler {
    val childState: StateFlow<ChildSlot<*, DialogChildComponent>>
}
