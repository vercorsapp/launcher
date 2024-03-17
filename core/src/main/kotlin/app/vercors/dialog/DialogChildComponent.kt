package app.vercors.dialog

interface DialogChildComponent {
    val onClose: () -> Unit
    fun close() = onClose()
}
