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