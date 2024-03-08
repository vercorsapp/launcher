package app.vercors.dialog

interface DialogEventHandler {
    fun openDialog(event: DialogEvent)
    fun closeDialog()
}