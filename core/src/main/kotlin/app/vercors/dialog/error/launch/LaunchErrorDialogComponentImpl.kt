package app.vercors.dialog.error.launch

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.logsFolder
import java.awt.Desktop

class LaunchErrorDialogComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit
) : AbstractAppComponent(componentContext), LaunchErrorDialogComponent {
    override fun openLogFolder() = Desktop.getDesktop().open(logsFolder.toFile())
}