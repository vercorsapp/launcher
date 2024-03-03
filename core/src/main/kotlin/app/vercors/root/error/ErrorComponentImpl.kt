package app.vercors.root.error

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext

class ErrorComponentImpl(
    componentContext: AppComponentContext,
    override val onClose: () -> Unit,
    override val error: Throwable
) : AbstractAppComponent(componentContext), ErrorComponent