package app.vercors.toolbar

import app.vercors.navigation.NavigationConfig
import app.vercors.notification.NotificationLevel

data class ToolbarUiState(
    val title: List<NavigationConfig> = emptyList(),
    val hasPreviousScreen: Boolean = false,
    val hasNextScreen: Boolean = false,
    val canRefreshScreen: Boolean = true,
    val unreadNotifications: Int = 0,
    val maxUnreadNotificationLevel: NotificationLevel = NotificationLevel.INFO
)
