package app.vercors.dialog

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.error.ErrorDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.login.LoginDialogComponent
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

class DialogComponentImpl(
    componentContext: AppComponentContext,
    private val dialogService: DialogService = componentContext.inject()
) : AbstractAppComponent(componentContext), DialogEventHandler by dialogService, DialogComponent {
    private val navigation = SlotNavigation<DialogConfig>()
    private val _childState: Value<ChildSlot<DialogConfig, DialogChildComponent>> = childSlot(
        source = navigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = childFactory(::createChild)
    )
    override val childState: StateFlow<ChildSlot<*, DialogChildComponent>> = _childState.toStateFlow()

    init {
        dialogService.dialogState.collectInLifecycle { config ->
            config?.let { navigation.activate(it) } ?: navigation.dismiss()
        }
    }

    private fun createChild(config: DialogConfig, componentContext: AppComponentContext): DialogChildComponent =
        when (config) {
            is DialogConfig.CreateInstance -> inject<CreateInstanceDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Login -> inject<LoginDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Error -> inject<ErrorDialogComponent>(componentContext, ::closeDialog)
        }
}