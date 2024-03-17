package app.vercors.dialog.error.javaversion

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.instance.InstanceData
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationService

class JavaVersionErrorDialogComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    override val instance: InstanceData,
    override val javaVersion: Int,
    private val navigationService: NavigationService = componentContext.inject()
) : AbstractAppComponent(componentContext), JavaVersionErrorDialogComponent {
    override fun openSettings() {
        navigationService.navigateTo(NavigationConfig.Configuration)
        close()
    }
}