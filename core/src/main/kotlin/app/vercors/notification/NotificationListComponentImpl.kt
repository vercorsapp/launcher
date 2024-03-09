package app.vercors.notification

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NotificationListComponentImpl(
    componentContext: AppComponentContext,
    notificationService: NotificationService = componentContext.inject()
) : AbstractAppComponent(componentContext), NotificationEventHandler by notificationService, NotificationListComponent {
    private val _uiState: MutableStateFlow<NotificationsUiState> = MutableStateFlow(NotificationsUiState())
    override val uiState: StateFlow<NotificationsUiState> = _uiState

    init {
        notificationService.notificationsState.collectInLifecycle { notifs ->
            _uiState.update { it.copy(notifications = notifs) }
        }
    }

    override fun onTogglePopup() {
        _uiState.update { it.copy(isPopupOpen = !it.isPopupOpen) }
    }
}