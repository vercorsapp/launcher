package app.vercors.root

import app.vercors.common.AppComponentContext
import app.vercors.componentContext
import app.vercors.configuration.ConfigurationData
import app.vercors.configuration.ConfigurationLoadingState
import app.vercors.configuration.ConfigurationService
import app.vercors.di.DI
import app.vercors.di.single
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent
import app.vercors.runTest
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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
    fun `given component is stopped, when config loading state changes, then child component is not updated`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()
            lifecycle.destroy()

            // when
            loadingState.update { ConfigurationLoadingState.Errored(Exception()) }

            // then
            assertNotSame(errorComponent, component.childState.first().child?.instance)
        }

    @Test
    fun `given component is started, when config loading state changes to errored, then set child component to error component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            loadingState.update { ConfigurationLoadingState.Errored(Exception()) }

            // then
            assertSame(errorComponent, component.childState.first().child?.instance)
        }

    @Test
    fun `given component is started, when config loading state changes to not loaded, then set child component to setup component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            loadingState.update { ConfigurationLoadingState.NotLoaded }

            // then
            assertSame(setupComponent, component.childState.first().child?.instance)
        }

    @Test
    fun `given component is started, when config loading state changes to loaded, then set child component to main component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            loadingState.update { ConfigurationLoadingState.Loaded(ConfigurationData.DEFAULT) }

            // then
            assertSame(mainComponent, component.childState.first().child?.instance)
        }

    private fun createComponent(lifecycle: Lifecycle) =
        RootComponentImpl(componentContext(lifecycle, di), onClose, configurationService)
}