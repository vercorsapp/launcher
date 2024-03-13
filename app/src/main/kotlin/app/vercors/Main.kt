package app.vercors

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.vercors.common.AppComponentContext
import app.vercors.di.inject
import app.vercors.root.RootComponent
import app.vercors.root.RootContent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello world! $APP_NAME v$APP_VERSION" }
    val properties = loadProperties("/app.properties")
    logger.info { "Loaded application properties" }
    application(exitProcessOnExit = false) {
        val coroutineScope = rememberCoroutineScope()
        val lifecycle = LifecycleRegistry()
        val windowState = rememberWindowState(size = DpSize(1280.dp, 720.dp))
        LifecycleController(lifecycle, windowState)
        val di = AppDI(properties, coroutineScope)
        CompositionLocalProvider(LocalDI provides di) {
            val componentContext = inject<AppComponentContext>(DefaultComponentContext(lifecycle), di)
            val rootComponent = inject<RootComponent>(componentContext, ::onClose)
            RootContent(rootComponent, windowState, ::onClose)
        }
    }
    logger.info { "Goodbye!\n" }
}

private fun ApplicationScope.onClose() {
    logger.info { "Exiting application" }
    exitApplication()
}