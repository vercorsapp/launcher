package app.vercors.navigation

import app.vercors.common.AppComponentContext
import app.vercors.componentContext
import app.vercors.configuration.ConfigurationComponent
import app.vercors.di.DI
import app.vercors.di.single
import app.vercors.home.HomeComponent
import app.vercors.instance.InstanceData
import app.vercors.instance.InstanceListComponent
import app.vercors.instance.details.InstanceDetailsComponent
import app.vercors.project.ProjectData
import app.vercors.project.ProjectDetailsComponent
import app.vercors.project.SearchComponent
import app.vercors.runTest
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NavigationComponentImplTest {
    @MockK
    lateinit var navigationService: NavigationService

    @MockK
    lateinit var appComponentContext: AppComponentContext

    @MockK
    lateinit var homeComponent: HomeComponent

    @MockK
    lateinit var instanceListComponent: InstanceListComponent

    @MockK
    lateinit var searchComponent: SearchComponent

    @MockK
    lateinit var configurationComponent: ConfigurationComponent

    @MockK
    lateinit var instanceDetailsComponent: InstanceDetailsComponent

    @MockK
    lateinit var projectDetailsComponent: ProjectDetailsComponent

    @MockK
    lateinit var instanceData: InstanceData

    @MockK
    lateinit var projectData: ProjectData

    private val navigationState = MutableStateFlow(NavigationState(listOf(NavigationConfig.Home), 0))
    private val refreshChannel = Channel<Unit>()
    private lateinit var di: DI

    @BeforeEach
    fun setup() {
        every { navigationService.navigationState } returns navigationState
        every { navigationService.refreshChannel } returns refreshChannel
        di = DI {
            single { appComponentContext }
            single { homeComponent }
            single { instanceListComponent }
            single { searchComponent }
            single { configurationComponent }
            single { instanceDetailsComponent }
            single { projectDetailsComponent }
        }
    }

    @Test
    fun `given component is stopped, when navigation state changes, then child component is not updated`() = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()
        lifecycle.destroy()

        // when
        navigationState.update { NavigationState(listOf(NavigationConfig.Search), 0) }

        // then
        assertSame(homeComponent, component.childState.first().active.instance)
    }

    @Test
    fun `given component is stopped, when refresh channel receives event, then child component is not refreshed`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            createComponent(lifecycle)
            lifecycle.resume()
            lifecycle.destroy()

            // when
            refreshChannel.trySend(Unit)

            // then
            verify(exactly = 0) { homeComponent.refresh() }
        }

    @Test
    fun `given component is started, when navigation state changes to home, then set child component to home component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.Home), 0) }

            // then
            assertSame(homeComponent, component.childState.first().active.instance)
        }

    @Test
    fun `given component is started, when navigation state changes to instance list, then set child component to instance list component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.InstanceList), 0) }

            // then
            assertSame(instanceListComponent, component.childState.first().active.instance)
        }

    @Test
    fun `given component is started, when navigation state changes to search, then set child component to search component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.Search), 0) }

            // then
            assertSame(searchComponent, component.childState.first().active.instance)
        }

    @Test
    fun `given component is started, when navigation state changes to configuration, then set child component to configuration component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.Configuration), 0) }

            // then
            assertSame(configurationComponent, component.childState.first().active.instance)
        }

    @Test
    fun `given component is started, when navigation state changes to instance details, then set child component to instance details component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.InstanceDetails(instanceData)), 0) }

            // then
            assertSame(instanceDetailsComponent, component.childState.first().active.instance)
        }

    @Test
    fun `given component is started, when navigation state changes to project details, then set child component to project details component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.ProjectDetails(projectData)), 0) }

            // then
            assertSame(projectDetailsComponent, component.childState.first().active.instance)
        }

    @Test
    fun `given component is started, when refresh channel receives event, then child component is refreshed`() =
        runTest {
            // given
            every { homeComponent.refresh() } returns Unit
            val lifecycle = LifecycleRegistry()
            createComponent(lifecycle)
            lifecycle.resume()

            // when
            navigationState.update { NavigationState(listOf(NavigationConfig.Home), 0) }

            refreshChannel.trySend(Unit)
            // then
            verify(exactly = 1) { homeComponent.refresh() }
        }

    private fun createComponent(lifecycle: Lifecycle) =
        NavigationComponentImpl(componentContext(lifecycle, di), navigationService)
}