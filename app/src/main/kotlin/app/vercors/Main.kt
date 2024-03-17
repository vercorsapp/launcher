/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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