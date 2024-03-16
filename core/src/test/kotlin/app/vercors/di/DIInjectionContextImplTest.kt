package app.vercors.di

import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DIInjectionContextImplTest {
    @MockK
    lateinit var di: DI

    @Test
    fun `given two params, when call param twice, then return params in order`() {
        // given
        val param1 = "test1"
        val param2 = "test2"
        val injectionContext = DIInjectionContextImpl(di, arrayOf(param1, param2))

        // when
        val res1 = injectionContext.param<String>()
        val res2 = injectionContext.param<String>()

        // then
        assertSame(param1, res1)
        assertSame(param2, res2)
    }

    @Test
    fun `given one param, when call param twice, then return param then error`() {
        // given
        val param1 = "test1"
        val injectionContext = DIInjectionContextImpl(di, arrayOf(param1))

        // when
        val res1 = injectionContext.param<String>()
        val res2 = { injectionContext.param<String>() }

        // then
        assertSame(param1, res1)
        assertThrows<IndexOutOfBoundsException> { res2() }
    }
}