package app.vercors.launcher.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import app.vercors.launcher.app.presentation.ui.AppWindow
import app.vercors.launcher.app.setup.SetupWindow
import app.vercors.launcher.core.data.storage.Storage
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import org.koin.compose.KoinContext
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.logger.SLF4JLogger

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello World!" }
    val externalScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    val koin = externalScope.async(Dispatchers.Default) {
        startKoin { setupKoin(externalScope) }.koin
    }
    application(exitProcessOnExit = false) {
        KoinContext(context = runBlocking { koin.await() }) {
            val storageState by Storage.state.collectAsState()

            if (storageState.isSetup) {
                logger.info { "Application path is setup: ${storageState.strPath}" }
                AppWindow()
            } else {
                logger.info { "Application path is not setup - showing setup window" }
                SetupWindow()
            }
        }
    }
    externalScope.cancel()
    logger.info { "Goodbye World!" }
}

private fun KoinApplication.setupKoin(externalScope: CoroutineScope) {
    modules(
        module { single(named("externalScope")) { externalScope } },
        AppModule().module
    )
    logger(SLF4JLogger(Level.DEBUG))
}