package app.vercors.dialog

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.dialog.error.ErrorDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.login.LoginDialogComponent
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DialogComponentImpl(
    componentContext: AppComponentContext
) : AbstractAppComponent(componentContext), DialogComponent {
    private val dialogNavigation = SlotNavigation<DialogConfig>()
    private val _dialog: Value<ChildSlot<DialogConfig, DialogChildComponent>> = childSlot(
        source = dialogNavigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true
    ) { configuration, componentContext ->
        createDialog(configuration, appChildContext(componentContext))
    }
    override val dialog: Value<ChildSlot<*, DialogChildComponent>> = _dialog

    private fun createDialog(config: DialogConfig, componentContext: AppComponentContext): DialogChildComponent =
        when (config) {
            is DialogConfig.CreateInstance -> inject<CreateInstanceDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Login -> inject<LoginDialogComponent>(componentContext, ::closeDialog)
            is DialogConfig.Error -> inject<ErrorDialogComponent>(componentContext, ::closeDialog)
        }

    override fun openDialog(event: DialogEvent) {
        when (event) {
            DialogEvent.CreateInstance -> dialogNavigation.activate(DialogConfig.CreateInstance)
            DialogEvent.Login -> dialogNavigation.activate(DialogConfig.Login())
        }
    }

    override fun closeDialog() {
        dialogNavigation.dismiss()
    }

    @Serializable
    private sealed interface DialogConfig {
        @Serializable
        data object CreateInstance : DialogConfig

        @Serializable
        data class Login(val authenticationStateCollector: () -> Unit = {}) : DialogConfig

        @Serializable
        data class Error(val title: String, val message: List<String>) : DialogConfig
    }
}