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

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.spi.ContextAwareBase
import org.slf4j.LoggerFactory
import kotlin.io.path.absolutePathString

class LoggerConfigurator : ContextAwareBase(), Configurator {
    override fun configure(lc: LoggerContext): Configurator.ExecutionStatus {
        addInfo("Setting up custom logger")
        val logsPath = logsFolder

        // Encoder for all appenders
        val appEncoder = PatternLayoutEncoder().apply {
            context = lc
            pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{50} -%kvp- %msg%n"
            start()
        }

        // Console appender
        val stdout = ConsoleAppender<ILoggingEvent>().apply {
            context = lc
            name = "STDOUT"
            encoder = appEncoder
            start()
        }

        // Debug rolling file rolling policy
        val debugRollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
            context = lc
            fileNamePattern = logsPath.resolve("debug.%d{yyyy-MM-dd}.log").absolutePathString()
            maxHistory = 7
        }
        // Debug filter
        val debugFilter = ThresholdFilter().apply {
            context = lc
            setLevel("DEBUG")
            start()
        }
        // Debug rolling file appender
        val debugRollingFile = RollingFileAppender<ILoggingEvent>().apply {
            context = lc
            name = "DEBUG_ROLLING_FILE"
            file = logsPath.resolve("debug.log").absolutePathString()
            encoder = appEncoder
            rollingPolicy = debugRollingPolicy.also {
                it.setParent(this)
                it.start()
            }
            addFilter(debugFilter)
            start()
        }

        // App rolling file rolling policy
        val appRollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
            context = lc
            fileNamePattern = logsPath.resolve("vercors.%d{yyyy-MM-dd}.log").absolutePathString()
            maxHistory = 7
        }
        // Info filter
        val infoFilter = ThresholdFilter().apply {
            context = lc
            setLevel("INFO")
            start()
        }
        // App rolling file appender
        val appRollingFile = RollingFileAppender<ILoggingEvent>().apply {
            context = lc
            name = "ROLLING_FILE"
            file = logsPath.resolve("vercors.log").absolutePathString()
            encoder = appEncoder
            rollingPolicy = appRollingPolicy.also {
                it.setParent(this)
                it.start()
            }
            addFilter(infoFilter)
            start()
        }

        val rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME)
        rootLogger.level = Level.TRACE
        rootLogger.addAppender(stdout)
        rootLogger.addAppender(debugRollingFile)
        rootLogger.addAppender(appRollingFile)

        addInfo("Custom logger is set up")
        return Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
    }

    companion object {
        fun reload() {
            val lc = LoggerFactory.getILoggerFactory() as LoggerContext
            val rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME)
            rootLogger.info("Reloading logger configuration")
            rootLogger.detachAndStopAllAppenders()
            val configurator = LoggerConfigurator()
            configurator.setContext(lc)
            configurator.configure(lc)
        }
    }
}