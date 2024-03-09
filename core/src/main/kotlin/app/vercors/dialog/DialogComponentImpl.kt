package app.vercors.dialog

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.error.ErrorDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.login.LoginDialogComponent
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value

class DialogComponentImpl(
    componentContext: AppComponentContext,
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), DialogEventHandler by dialogService, DialogComponent {
    private val dialogNavigation = SlotNavigation<DialogConfig>()
    private val _dialog: Value<ChildSlot<DialogConfig, DialogChildComponent>> = childSlot(
        source = dialogNavigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true
    ) { configuration, componentContext ->
        createDialog(configuration, appChildContext(componentContext))
    }
    override val dialog: Value<ChildSlot<*, DialogChildComponent>> = _dialog

    init {
        dialogService.dialogState.collectInLifecycle { config ->
            config?.let { dialogNavigation.activate(it) } ?: dialogNavigation.dismiss()
        }
    }

    private fun createDialog(config: DialogConfig, componentContext: AppComponentContext): DialogChildComponent =
        when (config) {
            is DialogConfig.CreateInstance -> inject<CreateInstanceDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Login -> inject<LoginDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Error -> inject<ErrorDialogComponent>(componentContext, ::closeDialog)
        }
}