package app.vercors.configuration

import app.vercors.runTest
import app.vercors.system.storage.StorageService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.*

@ExtendWith(MockKExtension::class)
class ConfigurationRepositoryImplTest {
    @MockK
    lateinit var storageService: StorageService

    @MockK
    lateinit var path: Path

    @MockK
    lateinit var json: Json

    @MockK
    lateinit var config: ConfigurationData

    @MockK
    lateinit var inputStream: InputStream

    @MockK
    lateinit var outputStream: OutputStream
    private val staticClasses = arrayOf(
        "kotlinx.serialization.json.JvmStreamsKt",
        "java.nio.file.Files",
        "java.nio.file.Path",
        "kotlin.io.path.PathsKt__PathUtilsKt"
    )

    @BeforeEach
    fun setup() {
        mockkStatic(*staticClasses)
        every { storageService.configPath } returns path
        every { path.toString() } returns "path"
    }

    @AfterEach
    fun cleanup() {
        unmockkStatic(*staticClasses)
    }

    @Test
    fun testLoadConfigurationWhenFileExistsAndIsValid() = runTest {
        val store = ConfigurationRepositoryImpl(json, storageService, StandardTestDispatcher(testScheduler))
        every { path.isRegularFile(*anyVararg()) } returns true
        every { path.inputStream(*anyVararg()) } returns inputStream
        every { inputStream.close() } returns Unit
        every { json.decodeFromStream<ConfigurationData>(inputStream) } returns config

        val loadedConfig = store.load()
        assertSame(config, loadedConfig)
        verify { path.isRegularFile(*anyVararg()) }
        verify { path.inputStream(*anyVararg()) }
        verify { inputStream.close() }
        verify { json.decodeFromStream<ConfigurationData>(inputStream) }
        confirmVerified(json, inputStream)
    }

    @Test
    fun testLoadConfigurationWhenFileExistsAndIsInvalid() = runTest {
        val store = ConfigurationRepositoryImpl(json, storageService, StandardTestDispatcher(testScheduler))
        every { path.isRegularFile(*anyVararg()) } returns true
        every { path.inputStream(*anyVararg()) } returns inputStream
        every { inputStream.close() } returns Unit
        every { json.decodeFromStream<ConfigurationData>(inputStream) } throws Exception()

        assertThrows<ConfigurationRepositoryException>("Unable to load configuration") { store.load() }
        verify { path.isRegularFile(*anyVararg()) }
        verify { path.inputStream(*anyVararg()) }
        verify { inputStream.close() }
        verify { json.decodeFromStream<ConfigurationData>(inputStream) }
        confirmVerified(json, inputStream)
    }

    @Test
    fun testLoadConfigurationWhenFileDoesNotExist() = runTest {
        val store = ConfigurationRepositoryImpl(json, storageService, StandardTestDispatcher(testScheduler))
        every { path.isRegularFile(*anyVararg()) } returns false
        every { path.exists(*anyVararg()) } returns false
        every { path.createParentDirectories(*anyVararg()) } returns path
        every { path.outputStream(*anyVararg()) } returns outputStream
        every { outputStream.close() } returns Unit
        every { json.encodeToStream<ConfigurationData>(any(), outputStream) } returns Unit

        val loadedConfig = store.load()
        assertEquals(store.defaultConfig, loadedConfig)
        verify { path.isRegularFile(*anyVararg()) }
        verify { path.exists(*anyVararg()) }
        verify { path.createParentDirectories(*anyVararg()) }
        verify { path.outputStream(*anyVararg()) }
        verify { outputStream.close() }
        verify { json.encodeToStream<ConfigurationData>(any(), outputStream) }
        confirmVerified(json, outputStream)
    }

    @Test
    fun testLoadConfigurationWhenFileInvalid() = runTest {
        val store = ConfigurationRepositoryImpl(json, storageService, StandardTestDispatcher(testScheduler))
        every { path.isRegularFile(*anyVararg()) } returns false
        every { path.exists(*anyVararg()) } returns true

        assertThrows<ConfigurationRepositoryException>("Unable to load configuration") { store.load() }
        verify { path.isRegularFile(*anyVararg()) }
        verify { path.exists(*anyVararg()) }
    }

    @Test
    fun testSaveConfigurationSuccess() = runTest {
        val store = ConfigurationRepositoryImpl(json, storageService, StandardTestDispatcher(testScheduler))
        every { path.createParentDirectories(*anyVararg()) } returns path
        every { path.outputStream(*anyVararg()) } returns outputStream
        every { outputStream.close() } returns Unit
        every { json.encodeToStream<ConfigurationData>(any(), outputStream) } returns Unit

        store.save(config)
        verify { path.createParentDirectories(*anyVararg()) }
        verify { path.outputStream(*anyVararg()) }
        verify { outputStream.close() }
        verify { json.encodeToStream<ConfigurationData>(any(), outputStream) }
        confirmVerified(json, outputStream)
    }

    @Test
    fun testSaveConfigurationError() = runTest {
        val store = ConfigurationRepositoryImpl(json, storageService, StandardTestDispatcher(testScheduler))
        every { path.createParentDirectories(*anyVararg()) } returns path
        every { path.outputStream(*anyVararg()) } returns outputStream
        every { outputStream.close() } returns Unit
        every { json.encodeToStream<ConfigurationData>(any(), outputStream) } throws Exception()

        assertThrows<ConfigurationRepositoryException>("Unable to save configuration") { store.save(config) }
        verify { path.createParentDirectories(*anyVararg()) }
        verify { path.outputStream(*anyVararg()) }
        verify { outputStream.close() }
        verify { json.encodeToStream<ConfigurationData>(any(), outputStream) }
        confirmVerified(json, outputStream)
    }
}