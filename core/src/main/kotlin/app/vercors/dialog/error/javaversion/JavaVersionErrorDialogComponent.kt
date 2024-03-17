package app.vercors.dialog.error.javaversion

import app.vercors.dialog.error.ErrorDialogComponent
import app.vercors.instance.InstanceData

interface JavaVersionErrorDialogComponent : ErrorDialogComponent {
    val instance: InstanceData
    val javaVersion: Int

    fun openSettings()
}