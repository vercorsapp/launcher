package app.vercors.root

import app.vercors.appBasePath
import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationLoadingState
import app.vercors.configuration.ConfigurationService
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class RootComponentImpl(
    componentContext: AppComponentContext,
    private val onClose: () -> Unit,
    configurationService: ConfigurationService = componentContext.inject()
) : AbstractAppComponent(componentContext), RootComponent {
    private val navigation = SlotNavigation<RootContentConfig>()
    private val _childState: Value<ChildSlot<RootContentConfig, RootChildComponent>> = childSlot(
        source = navigation,
        serializer = RootContentConfig.serializer(),
        childFactory = childFactory(::createChild)
    )
    override val childState: StateFlow<ChildSlot<*, RootChildComponent>> = _childState.toStateFlow()

    init {
        if (appBasePath != null) configurationService.load()
        configurationService.loadingState.collectInLifecycle(errorHandler = { onError(it) }) {
            when (it) {
                is ConfigurationLoadingState.Errored -> onError(it.error)
                is ConfigurationLoadingState.Loaded -> navigation.activate(RootContentConfig.Main)
                ConfigurationLoadingState.NotLoaded -> navigation.activate(RootContentConfig.Setup)
            }
        }
    }

    private fun onError(error: Throwable) = navigation.activate(RootContentConfig.Errored(error))

    private fun createChild(config: RootContentConfig, context: AppComponentContext): RootChildComponent {
        return when (config) {
            is RootContentConfig.Errored -> inject<ErrorComponent>(context, onClose, config.error)
            is RootContentConfig.Setup -> inject<SetupComponent>(context, onClose)
            is RootContentConfig.Main -> inject<MainComponent>(context, onClose)
        }
    }

    @Serializable
    private sealed interface RootContentConfig {
        data class Errored(val error: Throwable) : RootContentConfig
        data object Setup : RootContentConfig
        data object Main : RootContentConfig
    }
}