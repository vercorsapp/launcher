package app.vercors.dialog.login

import app.vercors.dialog.DialogChildComponent
import kotlinx.coroutines.flow.StateFlow

interface LoginDialogComponent : DialogChildComponent {
    val uiState: StateFlow<LoginDialogUiState>
    val canOpenInBrowser: Boolean

    fun openInBrowser(url: String)
}
