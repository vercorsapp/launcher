package app.vercors.root

import app.vercors.common.AbstractAppComponent
import app.vercors.common.AppComponentContext
import app.vercors.common.inject
import app.vercors.configuration.ConfigurationData
import app.vercors.configuration.ConfigurationLoadingState
import app.vercors.configuration.ConfigurationService
import app.vercors.onError
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
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
        configurationService.load()
        scope.launch {
            configurationService.loadingState.collect {
                when (it) {
                    is ConfigurationLoadingState.Errored -> onError(it.error)
                    is ConfigurationLoadingState.Loaded -> navigation.activate(
                        if (it.config.path === null) RootContentConfig.Setup(it.config)
                        else RootContentConfig.Main
                    )

                    ConfigurationLoadingState.NotLoaded -> navigation.dismiss()
                }
            }
        }.onError { onError(it) }
    }

    private fun onError(error: Throwable) = navigation.activate(RootContentConfig.Errored(error))

    private fun createChild(config: RootContentConfig, context: ComponentContext): RootChildComponent {
        val ctx = inject<AppComponentContext>(context, di)
        return when (config) {
            is RootContentConfig.Errored -> inject<ErrorComponent>(ctx, onClose, config.error)
            is RootContentConfig.Setup -> inject<SetupComponent>(ctx, onClose, config.config)
            is RootContentConfig.Main -> inject<MainComponent>(ctx, onClose)
        }
    }

    @Serializable
    private sealed interface RootContentConfig {
        data class Errored(val error: Throwable) : RootContentConfig
        data class Setup(val config: ConfigurationData) : RootContentConfig
        data object Main : RootContentConfig
    }
}