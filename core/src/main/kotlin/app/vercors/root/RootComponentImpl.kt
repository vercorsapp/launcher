/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
import com.arkivanov.decompose.value.operator.map
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class RootComponentImpl(
    componentContext: AppComponentContext,
    private val onClose: () -> Unit,
    configurationService: ConfigurationService = componentContext.inject()
) : AbstractAppComponent(componentContext), RootComponent {
    private val navigation = SlotNavigation<RootContentConfig>()
    private val _state: Value<ChildSlot<RootContentConfig, RootChildComponent>> = childSlot(
        source = navigation,
        serializer = RootContentConfig.serializer(),
        childFactory = childFactory(::createChild)
    )
    override val state: StateFlow<RootState> = _state
        .map { RootState(it.child?.instance) }
        .toStateFlow()

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

    override fun onIntent(intent: RootIntent) = when (intent) {
        RootIntent.Close -> onClose()
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