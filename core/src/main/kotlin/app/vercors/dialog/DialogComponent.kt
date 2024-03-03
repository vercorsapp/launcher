package app.vercors.dialog

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface DialogComponent {
    val dialog: Value<ChildSlot<*, DialogChildComponent>>
    fun openDialog(event: DialogEvent)
    fun closeDialog()
}
