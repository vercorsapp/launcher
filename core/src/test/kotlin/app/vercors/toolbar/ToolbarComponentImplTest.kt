package app.vercors.toolbar

import app.vercors.componentContext
import app.vercors.initMainDispatcher
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationService
import app.vercors.navigation.NavigationState
import app.vercors.notification.NotificationData
import app.vercors.notification.NotificationLevel
import app.vercors.notification.NotificationService
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class ToolbarComponentImplTest {
    @MockK
    lateinit var onToolbarClick: (ToolbarButton) -> Unit

    @MockK
    lateinit var onNotificationButtonClick: () -> Unit

    @MockK
    lateinit var navigationService: NavigationService

    @MockK
    lateinit var notificationService: NotificationService

    private var navigationState = MutableStateFlow(NavigationState(listOf(NavigationConfig.Home), 0))
    private val notificationsState = MutableStateFlow<List<NotificationData>>(emptyList())

    @BeforeEach
    fun setup() {
        every { navigationService.navigationState } returns navigationState
        every { notificationService.notificationsState } returns notificationsState
    }

    @ParameterizedTest
    @MethodSource("provide navigation states")
    fun `given component is started, when navigation state changes, ui state is updated accordingly`(
        navigation: NavigationState
    ) = runTest {
        initMainDispatcher()
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.start()

        navigationState.update { navigation }

        component.uiState.first().let {
            assertEquals(navigation.hasPreviousScreen, it.hasPreviousScreen)
            assertEquals(navigation.hasNextScreen, it.hasNextScreen)
        }
    }

    @Test
    fun `given component is stopped, when navigation state changes, ui state is not updated`() = runTest {
        initMainDispatcher()

        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.start()
        lifecycle.stop()

        val initialComponentState = component.uiState.value
        navigationState.update { NavigationState(listOf(NavigationConfig.Home, NavigationConfig.InstanceList), 1) }

        assertSame(initialComponentState, component.uiState.first())
    }

    @ParameterizedTest
    @MethodSource("provide notifications states")
    fun `given component is started, when notifications state changes, ui state is updated accordingly`(
        notifications: List<NotificationData>,
        unreadNotifications: Int,
        maxUnreadNotificationLevel: NotificationLevel
    ) = runTest {
        initMainDispatcher()

        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.start()

        notificationsState.update { notifications }

        component.uiState.first().let {
            assertEquals(unreadNotifications, it.unreadNotifications)
            assertEquals(maxUnreadNotificationLevel, it.maxUnreadNotificationLevel)
        }
    }

    @Test
    fun `given component is stopped, when notifications state changes, ui state is not updated`() = runTest {
        initMainDispatcher()

        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.start()
        lifecycle.stop()

        val initialComponentState = component.uiState.value
        notificationsState.update { listOf(NotificationData(NotificationLevel.INFO, "test")) }

        assertSame(initialComponentState, component.uiState.first())
    }

    @Test
    fun `given component is started, when title clicked, navigation service is called`() = runTest {
        initMainDispatcher()

        every { navigationService.navigateTo(any()) } returns Unit

        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.start()

        component.onTitleClick(NavigationConfig.Home)
        verify(exactly = 1) { navigationService.navigateTo(NavigationConfig.Home) }

        component.onTitleClick(NavigationConfig.Search)
        verify(exactly = 1) { navigationService.navigateTo(NavigationConfig.Search) }
    }

    private fun createComponent(lifecycle: Lifecycle) = ToolbarComponentImpl(
        componentContext = componentContext(lifecycle),
        onToolbarClick = onToolbarClick,
        onNotificationButtonClick = onNotificationButtonClick,
        navigationService = navigationService,
        notificationService = notificationService
    )

    companion object {
        @JvmStatic
        fun `provide navigation states`(): Stream<Arguments> = Stream.of(
            Arguments.of(
                NavigationState(
                    items = listOf(NavigationConfig.Home, NavigationConfig.InstanceList),
                    index = 1
                )
            ),
            Arguments.of(
                NavigationState(
                    items = listOf(NavigationConfig.Home, NavigationConfig.InstanceList, NavigationConfig.Search),
                    index = 2
                )
            ),
            Arguments.of(
                NavigationState(
                    items = listOf(NavigationConfig.Home, NavigationConfig.InstanceList, NavigationConfig.Search),
                    index = 1
                )
            ),
            Arguments.of(
                NavigationState(
                    items = listOf(
                        NavigationConfig.Home,
                        NavigationConfig.InstanceList,
                        NavigationConfig.Configuration
                    ),
                    index = 2
                )
            ),
            Arguments.of(
                NavigationState(
                    items = listOf(NavigationConfig.Home),
                    index = 0
                )
            )
        )

        @JvmStatic
        fun `provide notifications states`(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test"
                    ),
                ), 1, NotificationLevel.INFO
            ),
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test1"
                    ),
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test2"
                    )
                ), 2, NotificationLevel.INFO
            ),
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test1"
                    ),
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test2",
                        isRead = true
                    )
                ), 1, NotificationLevel.INFO
            ),
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test1"
                    ),
                    NotificationData(
                        level = NotificationLevel.ERROR,
                        text = "test2"
                    )
                ), 2, NotificationLevel.ERROR
            ),
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test1",
                        isRead = true
                    ),
                    NotificationData(
                        level = NotificationLevel.ERROR,
                        text = "test2"
                    )
                ), 1, NotificationLevel.ERROR
            ),
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO,
                        text = "test1"
                    ),
                    NotificationData(
                        level = NotificationLevel.ERROR,
                        text = "test2",
                        isRead = true
                    )
                ), 1, NotificationLevel.INFO
            )
        )
    }
}