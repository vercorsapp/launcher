package com.skyecodes.vercors.component.dialog

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.skyecodes.vercors.component.AbstractComponent
import com.skyecodes.vercors.component.AppComponentContext
import com.skyecodes.vercors.component.get
import com.skyecodes.vercors.data.model.app.Account
import com.skyecodes.vercors.data.service.AccountService
import com.skyecodes.vercors.data.service.AuthenticationState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI

interface AddAccountDialogComponent {
    val onClose: () -> Unit
    val uiState: StateFlow<UiState>
    val canOpenInBrowser: Boolean

    fun openInBrowser(url: String)

    data class UiState(
        val url: String? = null,
        val progress: Float = -1f,
        val error: Throwable? = null,
        val account: Account? = null
    ) {
        val canClose = account != null || error != null
        val isWaitingLogin = progress < 0 && error == null
        val hasUrl = url != null && isWaitingLogin && error == null
        val isSuccess = account != null && error == null
    }
}

class DefaultAddAccountDialogComponent(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val accountService: AccountService = componentContext.get()
) : AbstractComponent(componentContext), AddAccountDialogComponent {
    override val uiState = MutableStateFlow(AddAccountDialogComponent.UiState())
    override val canOpenInBrowser = Desktop.isDesktopSupported()
    private var authenticationJob: Job? = null

    init {
        doOnCreate { onCreate() }
        doOnDestroy { onDestroy() }
    }

    private fun onCreate() {
        authenticationJob = scope.launch {
            accountService.startAuthentication().collect { state ->
                when (state) {
                    is AuthenticationState.Error -> uiState.update { it.copy(error = state.error) }
                    is AuthenticationState.Progress -> uiState.update { it.copy(progress = state.progress) }
                    is AuthenticationState.Success -> uiState.update { it.copy(account = state.account) }
                    is AuthenticationState.Waiting -> uiState.update { it.copy(url = state.url) }
                }
            }
        }
    }

    private fun onDestroy() {
        authenticationJob?.cancel()
    }

    override fun openInBrowser(url: String) {
        Desktop.getDesktop().browse(URI(url))
    }
}