package app.vercors.root

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import app.vercors.root.error.ErrorComponent
import app.vercors.root.main.MainComponent
import app.vercors.root.setup.SetupComponent
import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RootContentTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var windowScope: WindowScope

    @MockK
    lateinit var windowState: WindowState

    @MockK
    lateinit var component: RootComponent

    @MockK
    lateinit var child: Value<ChildSlot<*, RootChildComponent>>

    @MockK
    lateinit var childValue: ChildSlot<*, RootChildComponent>

    @MockK
    lateinit var created: Child.Created<*, RootChildComponent>

    @MockK
    lateinit var mainComponent: MainComponent

    @MockK
    lateinit var setupComponent: SetupComponent

    @MockK
    lateinit var errorComponent: ErrorComponent

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setup() {
        every { component.child } returns child
        every { child.value } returns childValue
        every { child.subscribe(any()) } returns Cancellation { }
    }

    private fun verifyCommon() {
        verify { component.child }
        verify { child.value }
        verify { child.subscribe(any()) }
        verify { childValue.child }
        confirmVerified(component, child, childValue)
    }

    @Test
    fun testEmptyChildShowsNothing() {
        every { childValue.child } returns null
        rule.setContent { RootContent(component, windowState) {} }
        verifyCommon()
    }

    /*@Test
    fun testAppChildShowsApp() {
        mockkStatic("app.vercors.root.AppContentKt")
        every { childValue.child } returns created
        every { created.instance } returns appComponent
        everyComposable { AppContent(appComponent) } returns Unit
        rule.setContent { RootContent(component) }
        verifyCommon()
        verify { created.instance }
        verifyComposable { AppContent(appComponent) }
        confirmVerified(created, appComponent)
        unmockkStatic("app.vercors.root.AppContentKt")
    }*/
}