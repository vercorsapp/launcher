package app.vercors.navigation

interface NavigationEventHandler {
    fun handle(event: NavigationEvent)
    fun navigateTo(config: NavigationConfig)
}
