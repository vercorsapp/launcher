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

package app.vercors.toolbar

import app.vercors.componentContext
import app.vercors.navigation.NavigationConfig
import app.vercors.navigation.NavigationInternalState
import app.vercors.navigation.NavigationService
import app.vercors.notification.NotificationData
import app.vercors.notification.NotificationLevel
import app.vercors.notification.NotificationService
import app.vercors.runTest
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class ToolbarInteractiveDefaultComponentImplTest {
    @MockK
    lateinit var onToolbarClick: (ToolbarButton) -> Unit

    @MockK
    lateinit var onNotificationButtonClick: () -> Unit

    @MockK
    lateinit var navigationService: NavigationService

    @MockK
    lateinit var notificationService: NotificationService

    private var navigationState = MutableStateFlow(NavigationInternalState(listOf(NavigationConfig.Home), 0))
    private val notificationsState = MutableStateFlow<List<NotificationData>>(emptyList())

    @BeforeEach
    fun setup() {
        every { navigationService.navigationState } returns navigationState
        every { notificationService.notificationsState } returns notificationsState
    }

    @ParameterizedTest
    @MethodSource("provide navigation states")
    fun `given component is started, when navigation state changes, ui state is updated accordingly`(
        navigation: NavigationInternalState
    ) = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()

        // when
        navigationState.update { navigation }

        // then
        component.state.first().let {
            assertEquals(navigation.hasPreviousScreen, it.hasPreviousScreen)
            assertEquals(navigation.hasNextScreen, it.hasNextScreen)
        }
    }

    @Test
    fun `given component is stopped, when navigation state changes, ui state is not updated`() = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()
        lifecycle.destroy()

        // when
        val initialComponentState = component.state.value
        navigationState.update {
            NavigationInternalState(
                listOf(NavigationConfig.Home, NavigationConfig.InstanceList),
                1
            )
        }

        // then
        assertSame(initialComponentState, component.state.first())
    }

    @ParameterizedTest
    @MethodSource("provide notifications states")
    fun `given component is started, when notifications state changes, ui state is updated accordingly`(
        notifications: List<NotificationData>, unreadNotifications: Int, maxUnreadNotificationLevel: NotificationLevel
    ) = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()

        // when
        notificationsState.update { notifications }

        // then
        component.state.first().let {
            assertEquals(unreadNotifications, it.unreadNotifications)
            assertEquals(maxUnreadNotificationLevel, it.maxUnreadNotificationLevel)
        }
    }

    @Test
    fun `given component is stopped, when notifications state changes, ui state is not updated`() = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()
        lifecycle.destroy()

        // when
        val initialComponentState = component.state.value
        notificationsState.update { listOf(NotificationData(NotificationLevel.INFO, "test")) }

        // then
        assertSame(initialComponentState, component.state.first())
    }

    @Test
    fun `given component is started, when title clicked, navigation service is called`() = runTest {
        // given
        every { navigationService.navigateTo(any()) } returns Unit
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()

        // when
        component.onIntent(ToolbarIntent.TitleClick(NavigationConfig.Search))

        // then
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
                NavigationInternalState(
                    items = listOf(NavigationConfig.Home, NavigationConfig.InstanceList), index = 1
                )
            ), Arguments.of(
                NavigationInternalState(
                    items = listOf(NavigationConfig.Home, NavigationConfig.InstanceList, NavigationConfig.Search),
                    index = 2
                )
            ), Arguments.of(
                NavigationInternalState(
                    items = listOf(NavigationConfig.Home, NavigationConfig.InstanceList, NavigationConfig.Search),
                    index = 1
                )
            ), Arguments.of(
                NavigationInternalState(
                    items = listOf(
                        NavigationConfig.Home, NavigationConfig.InstanceList, NavigationConfig.Configuration
                    ), index = 2
                )
            ), Arguments.of(
                NavigationInternalState(
                    items = listOf(NavigationConfig.Home), index = 0
                )
            )
        )

        @JvmStatic
        fun `provide notifications states`(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO, text = "test"
                    ),
                ), 1, NotificationLevel.INFO
            ), Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO, text = "test1"
                    ), NotificationData(
                        level = NotificationLevel.INFO, text = "test2"
                    )
                ), 2, NotificationLevel.INFO
            ), Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO, text = "test1"
                    ), NotificationData(
                        level = NotificationLevel.INFO, text = "test2", isRead = true
                    )
                ), 1, NotificationLevel.INFO
            ), Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO, text = "test1"
                    ), NotificationData(
                        level = NotificationLevel.ERROR, text = "test2"
                    )
                ), 2, NotificationLevel.ERROR
            ), Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO, text = "test1", isRead = true
                    ), NotificationData(
                        level = NotificationLevel.ERROR, text = "test2"
                    )
                ), 1, NotificationLevel.ERROR
            ), Arguments.of(
                listOf(
                    NotificationData(
                        level = NotificationLevel.INFO, text = "test1"
                    ), NotificationData(
                        level = NotificationLevel.ERROR, text = "test2", isRead = true
                    )
                ), 1, NotificationLevel.INFO
            )
        )
    }
}