package app.vercors.dialog

interface DialogEventHandler {
    fun openDialog(config: DialogConfig)
    fun closeDialog()
}