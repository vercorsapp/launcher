package app.vercors.di

import app.vercors.runTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DIBuilderImplTest {
    @MockK
    lateinit var supplier: () -> TestElement

    @Test
    fun `given single in DI, when build DI, then should create element`() = runTest {
        // given
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            single { supplier() }
        }

        // when
        di.awaitInit()

        // then
        verify(exactly = 1) { supplier() }
    }

    @Test
    fun `given single in DI, when inject twice, then should return same element twice`() = runTest {
        // given
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            single { supplier() }
        }

        // when
        di.awaitInit()
        val a = di.inject<TestElement>()
        val b = di.inject<TestElement>()

        // then
        verify(exactly = 1) { supplier() }
        verify(exactly = 1) { supplier() }
        assertSame(a, b)
    }

    @Test
    fun `given lazy single in DI, when build DI, then should not create element`() = runTest {
        // given
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            lazySingle { supplier() }
        }
        // when
        di.awaitInit()

        // then
        verify(exactly = 0) { supplier() }
    }

    @Test
    fun `given lazy single in DI, when inject twice, then should return same element twice`() = runTest {
        // given
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            lazySingle { supplier() }
        }

        // when
        val a = di.inject<TestElement>()
        val b = di.inject<TestElement>()

        // then
        verify(exactly = 1) { supplier() }
        verify(exactly = 1) { supplier() }
        assertSame(a, b)
    }

    @Test
    fun `given factory in DI, when build DI, then should not create element`() = runTest {
        // given
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            factory { supplier() }
        }

        // when
        di.awaitInit()

        // then
        verify(exactly = 0) { supplier() }
    }

    @Test
    fun `given factory in DI, when inject twice, then should return different elements`() = runTest {
        // given
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            factory { supplier() }
        }

        // when
        val a = di.inject<TestElement>()
        val b = di.inject<TestElement>()

        // then
        verify(exactly = 2) { supplier() }
        assertNotSame(a, b)
    }

    class TestElement
}