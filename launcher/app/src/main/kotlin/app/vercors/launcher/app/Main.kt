/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.launcher.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import app.vercors.launcher.app.logging.AppLogbackConfigurator
import app.vercors.launcher.app.ui.AppWindow
import app.vercors.launcher.core.domain.APP_NAME
import app.vercors.launcher.core.domain.APP_VERSION
import app.vercors.launcher.core.storage.Storage
import app.vercors.launcher.setup.presentation.SetupWindow
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import okio.Path.Companion.toPath
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.fileProperties
import org.koin.ksp.generated.module
import org.koin.logger.SLF4JLogger

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello World!" }
    logger.info { "Launching $APP_NAME v$APP_VERSION" }
    val externalScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val koin = externalScope.async {
        startKoin {
            fileProperties()
            setupKoin(externalScope)
        }.koin
    }
    application(exitProcessOnExit = false) {
        KoinContext(context = runBlocking { koin.await() }) {
            val storage = koinInject<Storage>()
            val storageState by storage.state.collectAsState()

            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context)
                    .diskCache {
                        DiskCache.Builder()
                            .directory(storageState.path.toPath() / "cache" / "image")
                            .build()
                    }
                    .build()
            }

            if (storageState.isSetup) {
                logger.info { "Application path is setup - showing launcher window" }
                AppWindow()
            } else {
                logger.info { "Application path is not setup - showing setup window" }
                SetupWindow(
                    onLaunch = { AppLogbackConfigurator.instance.reload() }
                )
            }
        }
    }
    externalScope.cancel()
    logger.info { "Application stopped" }
}

private fun KoinApplication.setupKoin(externalScope: CoroutineScope) {
    modules(
        module { single { externalScope } },
        AppModule().module
    )
    logger(SLF4JLogger(Level.DEBUG))
}