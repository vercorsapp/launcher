package app.vercors.launcher.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import app.vercors.launcher.app.presentation.ui.App
import app.vercors.launcher.app.presentation.viewmodel.AppViewModel
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ksp.generated.module

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello World!" }
    val externalScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    application(exitProcessOnExit = false) {
        KoinApplication(application = { setupKoin(externalScope) }) {
            App()
        }
    }
    externalScope.cancel()
    logger.info { "Goodbye World!" }
}

private fun KoinApplication.setupKoin(externalScope: CoroutineScope) {
    modules(
        module {
            single(named("externalScope")) { externalScope }
            single(named("mainDispatcher")) { Dispatchers.Main.immediate } bind CoroutineDispatcher::class
            single(named("ioDispatcher")) { Dispatchers.IO }
            single(named("defaultDispatcher")) { Dispatchers.Default }
        },
        AppModule().module
    )
}