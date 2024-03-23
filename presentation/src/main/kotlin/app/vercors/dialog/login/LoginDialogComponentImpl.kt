/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.dialog.login

import app.vercors.account.AccountRepository
import app.vercors.account.auth.AuthenticateUseCase
import app.vercors.account.auth.AuthenticationState
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.openURL
import com.arkivanov.decompose.value.ObserveLifecycleMode
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.*
import java.awt.Desktop
import java.net.URI

internal class LoginDialogComponentImpl(
    componentContext: AppComponentContext,
    private val _onClose: () -> Unit,
    private val accountService: AccountRepository = componentContext.inject(),
    authenticateUseCase: AuthenticateUseCase = componentContext.inject()
) : AbstractAppComponent(componentContext, KotlinLogging.logger {}), LoginDialogComponent {
    private val _state = MutableStateFlow(LoginDialogState(Desktop.isDesktopSupported()))
    override val state: StateFlow<LoginDialogState> = _state
    private var cancelCallback: (() -> Unit)? = null

    init {
        authenticateUseCase()
            .catch { error -> if (error is Exception) _state.update { it.copy(error = error) } }
            .onCompletion { _ -> cancelCallback?.invoke() }
            .collectInLifecycle(ObserveLifecycleMode.CREATE_DESTROY) { state ->
                when (state) {
                    is AuthenticationState.Progress -> _state.update { it.copy(progress = state.progress) }
                    is AuthenticationState.Success -> {
                        accountService.addAccount(state.account)
                        _state.update { it.copy(account = state.account) }
                    }
                    is AuthenticationState.CancelCallback -> {
                        cancelCallback = state.callback
                    }

                    is AuthenticationState.Waiting -> _state.update { it.copy(url = state.url) }
                    AuthenticationState.Closed -> { /* Nothing */
                    }
                }
            }
    }

    override fun onIntent(intent: LoginDialogIntent) = when (intent) {
        LoginDialogIntent.CloseDialog -> onClose()
        is LoginDialogIntent.OpenBrowser -> openURL(URI(intent.url))
    }

    override fun onClose() = _onClose()
}