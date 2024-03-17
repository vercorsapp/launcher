package app.vercors.dialog

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
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
            DialogConfig.CreateInstance -> inject<CreateInstanceDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Login -> inject<LoginDialogComponent>(componentContext, ::closeDialog)
            DialogConfig.Error.Launch -> inject<LaunchErrorDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Error.JavaVersion -> inject<JavaVersionErrorDialogComponent>(
                componentContext,
                ::closeDialog,
                config.instance,
                config.javaVersion
            )
        }
}