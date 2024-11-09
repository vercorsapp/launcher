package app.vercors.launcher.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import app.vercors.launcher.app.logging.AppLogbackConfigurator
import app.vercors.launcher.app.ui.AppWindow
import app.vercors.launcher.app.viewmodel.AppViewModel
import app.vercors.launcher.core.storage.Storage
import app.vercors.launcher.setup.presentation.screen.SetupWindow
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
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
    val externalScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val koin = externalScope.async {
        startKoin { setupKoin(externalScope) }.koin
    }
    application(exitProcessOnExit = false) {
        KoinContext(context = runBlocking { koin.await() }) {
            val storage = koinInject<Storage>()
            val storageState by storage.state.collectAsState()

            if (storageState.isSetup) {
                val viewModel = koinInject<AppViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                AppWindow(
                    uiState = uiState
                )
            } else {
                logger.info { "Application path is not setup - showing setup window" }
                SetupWindow(
                    onLaunch = { AppLogbackConfigurator.instance.reload() }
                )
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