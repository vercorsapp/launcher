package app.vercors.dialog

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DialogServiceImpl : DialogService {
    private val _dialogState: MutableStateFlow<DialogConfig?> = MutableStateFlow(null)
    override val dialogState: StateFlow<DialogConfig?> = _dialogState

    override fun openDialog(config: DialogConfig) = update(config)

    override fun closeDialog() = update(null)

    private fun update(config: DialogConfig?) = _dialogState.update { config }
}