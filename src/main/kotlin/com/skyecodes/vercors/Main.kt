package com.skyecodes.vercors

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.skyecodes.vercors.component.DefaultAppComponentContext
import com.skyecodes.vercors.component.DefaultRootComponent
import com.skyecodes.vercors.data.service.*
import com.skyecodes.vercors.ui.AppWindow
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger
import java.util.*

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Vercors"

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello world! $APP_NAME v$APP_VERSION" }
    val properties = Properties().apply { load(resourceAsStream("/app.properties")) }
    logger.info { "Loaded application properties" }

    application(exitProcessOnExit = false) {
        val coroutineScope = rememberCoroutineScope()

        KoinApplication(application = {
            logger(SLF4JLogger())
            modules(module(createdAtStart = true) {
                single<Json> { AppJson }
                single<HttpClient> { appHttpClient(get()) }
                single<ConfigurationService> { ConfigurationServiceImpl(coroutineScope, get(), get()) }
                single<CurseforgeService> {
                    CurseforgeServiceImpl(
                        coroutineScope,
                        get(),
                        properties.getProperty("curseforgeApiKey")
                    )
                }
                single<InstanceService> { InstanceServiceImpl(coroutineScope, get(), get()) }
                single<ModrinthService> {
                    ModrinthServiceImpl(
                        coroutineScope,
                        get(),
                        properties.getProperty("modrinthApiKey")
                    )
                }
                single<MojangService> { MojangServiceImpl(coroutineScope, get()) }
                single<StorageService> { StorageServiceImpl() }
                single<AccountService> {
                    AccountServiceImpl(
                        coroutineScope,
                        get(),
                        get(),
                        get(),
                        properties.getProperty("microsoftClientId")
                    )
                }
                single<LauncherService> { LauncherServiceImpl(coroutineScope, get(), get(), get(), get(), get()) }
            })
        }) {
            val lifecycle = LifecycleRegistry()
            val koin = getKoin()
            val root = runOnUiThread {
                DefaultRootComponent(
                    componentContext = DefaultAppComponentContext(DefaultComponentContext(lifecycle), koin)
                )
            }
            AppWindow(root, lifecycle)
        }
    }
    logger.info { "Goodbye!\n" }
}
