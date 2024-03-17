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

import app.vercors.account.AccountService
import app.vercors.account.auth.AuthenticationService
import app.vercors.account.auth.AuthenticationState
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.openURL
import com.arkivanov.decompose.value.ObserveLifecycleMode
import kotlinx.coroutines.flow.*
import java.awt.Desktop
import java.net.URI

class LoginDialogComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    private val accountService: AccountService = componentContext.inject(),
    private val authenticationService: AuthenticationService = componentContext.inject()
) : AbstractAppComponent(componentContext), LoginDialogComponent {
    private val _uiState = MutableStateFlow(LoginDialogUiState())
    override val uiState: StateFlow<LoginDialogUiState> = _uiState
    override val canOpenInBrowser = Desktop.isDesktopSupported()

    init {
        authenticationService.startAuthentication()
            .catch { error -> _uiState.update { it.copy(error = error) } }
            .onCompletion { _ -> authenticationService.cancelAuthentication() }
            .collectInLifecycle(ObserveLifecycleMode.CREATE_DESTROY) { state ->
                when (state) {
                    is AuthenticationState.Progress -> _uiState.update { it.copy(progress = state.progress) }
                    is AuthenticationState.Success -> {
                        accountService.addAccount(state.account)
                        _uiState.update { it.copy(account = state.account) }
                    }

                    is AuthenticationState.Waiting -> _uiState.update { it.copy(url = state.url) }
                    AuthenticationState.Closed -> { /* Nothing */
                    }
                }
            }
    }

    override fun openInBrowser(url: String) {
        openURL(URI(url))
    }

    /*override fun close() {
        onClose()
        //authenticationStateCollector(AuthenticationState.Closed)
    }*/
}