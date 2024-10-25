package app.vercors.launcher

import androidx.compose.ui.window.application
import app.vercors.launcher.account.AccountModule
import app.vercors.launcher.app.AppModule
import app.vercors.launcher.app.presentation.ui.App
import app.vercors.launcher.core.CoreModule
import app.vercors.launcher.game.GameModule
import app.vercors.launcher.home.HomeModule
import app.vercors.launcher.instance.InstanceModule
import app.vercors.launcher.project.ProjectModule
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
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
        },
        CoreModule().module,
        AppModule().module,
        HomeModule().module,
        InstanceModule().module,
        ProjectModule().module,
        AccountModule().module,
        GameModule().module
    )
}