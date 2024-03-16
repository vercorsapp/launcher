package app.vercors.root

import app.vercors.common.AppComponentContext
import app.vercors.componentContext
import app.vercors.configuration.ConfigurationData
import app.vercors.configuration.ConfigurationLoadingState
import app.vercors.configuration.ConfigurationService
import app.vercors.di.DI
import app.vercors.di.single
import app.vercors.initMainDispatcher
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RootComponentImplTest {
    @MockK
    lateinit var onClose: () -> Unit

    @MockK
    lateinit var configurationService: ConfigurationService

    @MockK
    lateinit var appComponentContext: AppComponentContext

    @MockK
    lateinit var errorComponent: ErrorComponent

    @MockK
    lateinit var setupComponent: SetupComponent

    @MockK
    lateinit var mainComponent: MainComponent

    private val loadingState = MutableStateFlow<ConfigurationLoadingState>(ConfigurationLoadingState.NotLoaded)
    private lateinit var di: DI

    @BeforeEach
    fun setup() {
        every { configurationService.loadingState } returns loadingState
        di = DI {
            single { appComponentContext }
            single { errorComponent }
            single { setupComponent }
            single { mainComponent }
        }
    }

    @Test
    fun `given component is started, when config loading state changes, then component is not updated`() = runTest {
        initMainDispatcher()
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        val childState = component.run { child.toStateFlow() }

        loadingState.update { ConfigurationLoadingState.Errored(Exception()) }

        assertNull(childState.first().child)
    }

    @Test
    fun `given component is started, when config loading state is errored, then return error component`() = runTest {
        initMainDispatcher()
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        val childState = component.run { child.toStateFlow() }
        lifecycle.resume()

        loadingState.update { ConfigurationLoadingState.Errored(Exception()) }

        assertSame(errorComponent, childState.first().child?.instance)
    }

    @Test
    fun `given component is started, when config loading state is not loaded, then return setup component`() = runTest {
        initMainDispatcher()
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        val childState = component.run { child.toStateFlow() }
        lifecycle.resume()

        loadingState.update { ConfigurationLoadingState.NotLoaded }

        assertSame(setupComponent, childState.first().child?.instance)
    }

    @Test
    fun `given component is started, when config loading state is loaded, then return main component`() = runTest {
        initMainDispatcher()
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        val childState = component.run { child.toStateFlow() }
        lifecycle.resume()

        loadingState.update { ConfigurationLoadingState.Loaded(ConfigurationData.DEFAULT) }

        assertSame(mainComponent, childState.first().child?.instance)
    }

    private fun createComponent(lifecycle: Lifecycle) =
        RootComponentImpl(componentContext(lifecycle, di), onClose, configurationService)
}