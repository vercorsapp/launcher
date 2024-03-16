package app.vercors.di

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DIBuilderImplTest {
    @MockK
    lateinit var supplier: () -> TestElement

    @Test
    fun `given single in DI, when build DI, then should create element`() = runTest {
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            single { supplier() }
        }
        di.awaitInit()
        verify(exactly = 1) { supplier() }
    }

    @Test
    fun `given single in DI, when inject twice, then should return same element twice`() = runTest {
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            single { supplier() }
        }
        di.awaitInit()
        val a = di.inject<TestElement>()
        verify(exactly = 1) { supplier() }
        val b = di.inject<TestElement>()
        verify(exactly = 1) { supplier() }
        assertSame(a, b)
    }

    @Test
    fun `given lazy single in DI, when build DI, then should not create element`() = runTest {
        every { supplier() } answers { TestElement() }
        DI(this, StandardTestDispatcher(testScheduler)) {
            lazySingle { supplier() }
        }
        verify(exactly = 0) { supplier() }
    }

    @Test
    fun `given lazy single in DI, when inject twice, then should return same element twice`() = runTest {
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            lazySingle { supplier() }
        }
        val a = di.inject<TestElement>()
        verify(exactly = 1) { supplier() }
        val b = di.inject<TestElement>()
        verify(exactly = 1) { supplier() }
        assertSame(a, b)
    }

    @Test
    fun `given factory in DI, when build DI, then should not create element`() = runTest {
        every { supplier() } answers { TestElement() }
        DI(this, StandardTestDispatcher(testScheduler)) {
            factory { supplier() }
        }
        verify(exactly = 0) { supplier() }
    }

    @Test
    fun `given factory in DI, when inject twice, then should return different elements`() = runTest {
        every { supplier() } answers { TestElement() }
        val di = DI(this, StandardTestDispatcher(testScheduler)) {
            factory { supplier() }
        }
        val a = di.inject<TestElement>()
        verify(exactly = 1) { supplier() }
        val b = di.inject<TestElement>()
        verify(exactly = 2) { supplier() }
        assertNotSame(a, b)
    }

    class TestElement
}