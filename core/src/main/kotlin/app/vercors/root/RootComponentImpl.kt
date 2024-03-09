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
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable

private val logger = KotlinLogging.logger { }

class RootComponentImpl(
    componentContext: AppComponentContext,
    private val onClose: () -> Unit,
    private val configurationService: ConfigurationService = componentContext.inject()
) : AbstractAppComponent(componentContext), RootComponent {
    private val navigation = SlotNavigation<RootContentConfig>()
    override val child: Value<ChildSlot<*, RootChildComponent>> = childSlot(
        source = navigation,
        serializer = RootContentConfig.serializer(),
        childFactory = ::createChild
    )

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

    private fun createChild(config: RootContentConfig, context: ComponentContext): RootChildComponent {
        val ctx = inject<AppComponentContext>(context, di)
        return when (config) {
            is RootContentConfig.Errored -> inject<ErrorComponent>(ctx, onClose, config.error)
            is RootContentConfig.Setup -> inject<SetupComponent>(ctx, onClose)
            is RootContentConfig.Main -> inject<MainComponent>(ctx, onClose)
        }
    }

    @Serializable
    private sealed interface RootContentConfig {
        data class Errored(val error: Throwable) : RootContentConfig
        data object Setup : RootContentConfig
        data object Main : RootContentConfig
    }
}