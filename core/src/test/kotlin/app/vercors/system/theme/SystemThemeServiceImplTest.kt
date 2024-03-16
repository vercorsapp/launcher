package app.vercors.system.theme

import app.vercors.runTest
import com.jthemedetecor.OsThemeDetector
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.function.Consumer

@ExtendWith(MockKExtension::class)
class SystemThemeServiceImplTest {
    @MockK
    lateinit var osThemeDetector: OsThemeDetector

    @Test
    fun `given service is created, when initialization is done, initial state should be correct`() = runTest {
        // given
        every { osThemeDetector.isDark } returns true
        every { osThemeDetector.registerListener(any()) } returns Unit
        val service = SystemThemeServiceImpl(this) { osThemeDetector }

        // when
        service.awaitInit()

        // then
        assertTrue(service.isDark.value)
    }

    @Test
    fun `given service is created, when system theme changes, state should be updated`() = runTest {
        // given
        lateinit var callback: Consumer<Boolean>
        every { osThemeDetector.isDark } returns true
        every { osThemeDetector.registerListener(any()) } answers { callback = firstArg() }
        val service = SystemThemeServiceImpl(this) { osThemeDetector }

        // when
        service.awaitInit()
        callback.accept(false)

        // then
        assertFalse(service.isDark.first())
    }
}