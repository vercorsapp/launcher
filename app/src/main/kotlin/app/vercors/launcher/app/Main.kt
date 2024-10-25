package app.vercors.launcher.app

import androidx.compose.ui.window.application
import app.vercors.launcher.app.presentation.ui.App
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.logger.SLF4JLogger

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
        module { single(named("externalScope")) { externalScope } },
        AppModule().module
    )
    logger(SLF4JLogger(Level.DEBUG))
}