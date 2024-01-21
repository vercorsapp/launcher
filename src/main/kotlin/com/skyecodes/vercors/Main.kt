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
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Vercors"

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello world! $APP_NAME v$APP_VERSION" }
    application {
        val coroutineScope = rememberCoroutineScope()

        KoinApplication(application = {
            logger(SLF4JLogger())
            modules(module {
                single<ConfigurationService> { ConfigurationServiceImpl(coroutineScope, get()) }
                single<CurseforgeService> { CurseforgeServiceImpl(coroutineScope, get()) }
                single<HttpClient> { AppHttpClient }
                single<InstanceService> { InstanceServiceImpl(coroutineScope, get()) }
                single<ModrinthService> { ModrinthServiceImpl(coroutineScope, get()) }
                single<MojangService> { MojangServiceImpl(coroutineScope, get()) }
                single<StorageService> { StorageServiceImpl() }
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
}
