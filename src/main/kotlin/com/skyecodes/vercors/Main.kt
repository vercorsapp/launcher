package com.skyecodes.vercors

import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.skyecodes.vercors.component.DefaultAppComponentContext
import com.skyecodes.vercors.component.DefaultRootComponent
import com.skyecodes.vercors.data.service.*
import com.skyecodes.vercors.ui.AppWindow
import io.github.oshai.kotlinlogging.KotlinLogging
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
        KoinApplication(application = {
            logger(SLF4JLogger())
            modules(module {
                single<ConfigurationService> { ConfigurationServiceImpl(get()) }
                single<CurseforgeService> { CurseforgeServiceImpl(get()) }
                single<HttpService> { HttpServiceImpl() }
                single<InstanceService> { InstanceServiceImpl(get()) }
                single<ModrinthService> { ModrinthServiceImpl(get()) }
                single<MojangService> { MojangServiceImpl(get()) }
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
