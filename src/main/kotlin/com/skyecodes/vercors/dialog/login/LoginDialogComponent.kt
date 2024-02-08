package com.skyecodes.vercors.dialog.login

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.skyecodes.vercors.accounts.Account
import com.skyecodes.vercors.accounts.AccountService
import com.skyecodes.vercors.accounts.AuthenticationState
import com.skyecodes.vercors.root.AbstractComponent
import com.skyecodes.vercors.root.AppComponentContext
import com.skyecodes.vercors.root.get
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI

interface LoginDialogComponent {
    val uiState: StateFlow<UiState>
    val canOpenInBrowser: Boolean

    fun close()
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

class DefaultLoginDialogComponent(
    componentContext: AppComponentContext,
    private val onClose: () -> Unit,
    private val authenticationStateCollector: (AuthenticationState) -> Unit,
    private val accountService: AccountService = componentContext.get()
) : AbstractComponent(componentContext), LoginDialogComponent {
    override val uiState = MutableStateFlow(LoginDialogComponent.UiState())
    override val canOpenInBrowser = Desktop.isDesktopSupported()
    private var authenticationJob: Job? = null

    init {
        doOnCreate { onCreate() }
        doOnDestroy { onDestroy() }
    }

    private fun onCreate() {
        authenticationJob = scope.launch {
            accountService.startAuthentication().collect { state ->
                authenticationStateCollector(state)
                when (state) {
                    is AuthenticationState.Error -> uiState.update { it.copy(error = state.error) }
                    is AuthenticationState.Progress -> uiState.update { it.copy(progress = state.progress) }
                    is AuthenticationState.Success -> uiState.update { it.copy(account = state.account) }
                    is AuthenticationState.Waiting -> uiState.update { it.copy(url = state.url) }
                    else -> {}
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

    override fun close() {
        onClose()
        authenticationStateCollector(AuthenticationState.Closed)
    }
}