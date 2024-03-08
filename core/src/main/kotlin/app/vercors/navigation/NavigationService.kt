package app.vercors.navigation

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

interface NavigationService : NavigationEventHandler {
    val navigationState: StateFlow<NavigationState>
    val refreshChannel: ReceiveChannel<Unit>
}