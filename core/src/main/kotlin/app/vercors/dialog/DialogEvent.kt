package app.vercors.dialog

sealed interface DialogEvent {
    data object CreateInstance : DialogEvent
    data object Login : DialogEvent
}