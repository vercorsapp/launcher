package app.vercors.dialog

import kotlinx.coroutines.flow.StateFlow

interface DialogService : DialogEventHandler {
    val dialogState: StateFlow<DialogConfig?>
}