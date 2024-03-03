package app.vercors.dialog.login

import app.vercors.account.AccountService
import app.vercors.account.auth.AuthenticationService
import app.vercors.account.auth.AuthenticationState
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.openURL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI

class LoginDialogComponentImpl(
    componentContext: AppComponentContext,
    private val onClose: () -> Unit,
    private val accountService: AccountService = componentContext.inject(),
    private val authenticationService: AuthenticationService = componentContext.inject()
) : AbstractAppComponent(componentContext), LoginDialogComponent {
    private val _uiState = MutableStateFlow(LoginDialogUiState())
    override val uiState: StateFlow<LoginDialogUiState> = _uiState
    override val canOpenInBrowser = Desktop.isDesktopSupported()

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            authenticationService.startAuthentication()
                .catch { error -> _uiState.update { it.copy(error = error) } }
                .collect { state ->
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
    }

    override fun openInBrowser(url: String) {
        openURL(URI(url))
    }

    override fun close() {
        onClose()
        //authenticationStateCollector(AuthenticationState.Closed)
    }
}