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

package app.vercors.configuration

import app.vercors.runTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ConfigurationServiceImplTest {
    @MockK
    lateinit var store: ConfigurationRepository

    @MockK
    lateinit var config: ConfigurationData

    @MockK
    lateinit var config2: ConfigurationData

    @Test
    fun testInit() = runTest {
        val service = ConfigurationServiceImpl(backgroundScope, store)
        assertTrue(service.loadingState.value is ConfigurationLoadingState.NotLoaded)
        assertNull(service.configState.value)
    }

    @Test
    fun testLoadSuccess() = runTest {
        val service = ConfigurationServiceImpl(backgroundScope, store)
        coEvery { store.load() } coAnswers { delay(10); config }

        val job = service.load()
        job.join()
        assertTrue(job.isCompleted)
        coVerify(exactly = 1) { store.load() }
        confirmVerified(store)

        assertTrue(service.loadingState.value is ConfigurationLoadingState.Loaded)
        assertSame(config, (service.loadingState.value as ConfigurationLoadingState.Loaded).config)
        assertNotNull(service.configState.value)
        assertSame(config, service.configState.value)
    }

    @Test
    fun testLoadError() = runTest {
        val service = ConfigurationServiceImpl(backgroundScope, store)
        coEvery { store.load() } coAnswers { delay(10); throw ConfigurationRepositoryException("test") }

        val job = service.load()
        job.join()
        assertTrue(job.isCompleted)
        coVerify(exactly = 1) { store.load() }
        confirmVerified(store)

        assertTrue(service.loadingState.value is ConfigurationLoadingState.Errored)
        assertEquals("test", (service.loadingState.value as ConfigurationLoadingState.Errored).error.message)
        assertNull(service.configState.value)
    }

    @Test
    fun testUpdate() = runTest {
        val service = ConfigurationServiceImpl(backgroundScope, store)
        coEvery { store.load() } returns config
        coEvery { store.save(any()) } returns Unit

        service.load()
        delay(10)
        service.update(false) { config2 }
        delay(10)

        assertTrue(service.loadingState.value is ConfigurationLoadingState.Loaded)
        assertSame(config2, (service.loadingState.value as ConfigurationLoadingState.Loaded).config)
        assertNotNull(service.configState.value)
        assertSame(config2, service.configState.value)
    }

    @Test
    fun testAutoSaveOnUpdate() = runTest {
        val service = ConfigurationServiceImpl(backgroundScope, store)
        coEvery { store.load() } returns config
        coEvery { store.save(any()) } returns Unit

        service.load()
        delay(10)
        coVerify(exactly = 1) { store.load() }
        coVerify(exactly = 0) { store.save(any()) }
        confirmVerified(store)

        service.update(false) { config2 }
        delay(10)
        coVerify(exactly = 1) { store.save(config2) }
        confirmVerified(store)
    }
}