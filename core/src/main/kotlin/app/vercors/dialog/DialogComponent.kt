package app.vercors.dialog

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface DialogComponent : DialogEventHandler {
    val dialog: Value<ChildSlot<*, DialogChildComponent>>
}
