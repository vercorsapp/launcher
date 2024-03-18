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

package app.vercors.dialog

import app.vercors.common.AppComponentContext
import app.vercors.componentContext
import app.vercors.di.DI
import app.vercors.di.single
import app.vercors.dialog.error.javaversion.JavaVersionErrorDialogComponent
import app.vercors.dialog.error.launch.LaunchErrorDialogComponent
import app.vercors.dialog.instance.CreateInstanceDialogComponent
import app.vercors.dialog.login.LoginDialogComponent
import app.vercors.instance.InstanceData
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
class DialogInteractiveDefaultComponentImplTest {
    @MockK
    lateinit var dialogService: DialogService

    @MockK
    lateinit var appComponentContext: AppComponentContext

    @MockK
    lateinit var createInstanceDialogComponent: CreateInstanceDialogComponent

    @MockK
    lateinit var loginDialogComponent: LoginDialogComponent

    @MockK
    lateinit var launchErrorDialogComponent: LaunchErrorDialogComponent

    @MockK
    lateinit var javaVersionErrorDialogComponent: JavaVersionErrorDialogComponent

    @MockK
    lateinit var instanceData: InstanceData

    private val dialogState = MutableStateFlow<DialogConfig?>(null)
    private lateinit var di: DI

    @BeforeEach
    fun setup() {
        every { dialogService.dialogState } returns dialogState
        di = DI {
            single { appComponentContext }
            single { createInstanceDialogComponent }
            single { loginDialogComponent }
            single { launchErrorDialogComponent }
            single { javaVersionErrorDialogComponent }
        }
    }

    @Test
    fun `given component is stopped, when dialog state changes, then child component is not updated`() = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()
        lifecycle.destroy()

        // when
        dialogState.update { DialogConfig.CreateInstance }

        // then
        assertNull(component.state.first().child)
    }

    @Test
    fun `given component is started, when dialog state changes to null, then set child component to null`() = runTest {
        // given
        val lifecycle = LifecycleRegistry()
        val component = createComponent(lifecycle)
        lifecycle.resume()

        // when
        dialogState.update { null }

        // then
        assertNull(component.state.first().child)
    }

    @Test
    fun `given component is started, when dialog state changes to create instance, then set child component to create instance component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            dialogState.update { DialogConfig.CreateInstance }

            // then
            assertSame(createInstanceDialogComponent, component.state.first().child)
        }

    @Test
    fun `given component is started, when dialog state changes to launch error, then set child component to launch error component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            dialogState.update { DialogConfig.Error.Launch }

            // then
            assertSame(launchErrorDialogComponent, component.state.first().child)
        }

    @Test
    fun `given component is started, when dialog state changes to java version error, then set child component to java version error component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            dialogState.update { DialogConfig.Error.JavaVersion(instanceData, 0) }

            // then
            assertSame(javaVersionErrorDialogComponent, component.state.first().child)
        }

    @Test
    fun `given component is started, when dialog state changes to login, then set child component to login component`() =
        runTest {
            // given
            val lifecycle = LifecycleRegistry()
            val component = createComponent(lifecycle)
            lifecycle.resume()

            // when
            dialogState.update { DialogConfig.Login() }

            // then
            assertSame(loginDialogComponent, component.state.first().child)
        }

    private fun createComponent(lifecycle: Lifecycle) =
        DialogComponentImpl(componentContext(lifecycle, di), dialogService)
}