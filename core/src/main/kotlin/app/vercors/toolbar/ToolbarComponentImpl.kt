package app.vercors.toolbar

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationService
import app.vercors.notification.NotificationLevel
import app.vercors.notification.NotificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class ToolbarComponentImpl(
    componentContext: AppComponentContext,
    override val onToolbarClick: (ToolbarButton) -> Unit,
    override val onNotificationButtonClick: () -> Unit,
    private val navigationService: NavigationService = componentContext.inject(),
    private val notificationService: NotificationService = componentContext.inject()
) : AbstractAppComponent(componentContext), ToolbarComponent {
    private val _uiState = MutableStateFlow(ToolbarUiState())
    override val uiState: StateFlow<ToolbarUiState> = _uiState

    init {
        navigationService.navigationState.collectInLifecycle { state ->
            _uiState.update {
                it.copy(
                    title = listOf(state.active),
                    hasPreviousScreen = state.hasPreviousScreen,
                    hasNextScreen = state.hasNextScreen
                )
            }
        }
        notificationService.notificationsState
            .map { state -> state.filter { !it.isRead } }
            .collectInLifecycle { unreadNotifications ->
                _uiState.update {
                    it.copy(
                        unreadNotifications = unreadNotifications.count(),
                        maxUnreadNotificationLevel = unreadNotifications.maxOfOrNull { n -> n.level }
                            ?: NotificationLevel.INFO
                    )
                }
            }
    }

    override fun onTitleClick(config: NavigationConfig) {
        navigationService.navigateTo(config)
    }
}