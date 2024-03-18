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
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.spi.ContextAwareBase

class LoggerConfiguratorTest : ContextAwareBase(), Configurator {
    override fun configure(lc: LoggerContext): Configurator.ExecutionStatus {
        addInfo("Setting up custom logger")

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

        val rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME)
        rootLogger.level = Level.DEBUG
        rootLogger.addAppender(stdout)

        addInfo("Custom logger is set up")
        return Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
    }
}