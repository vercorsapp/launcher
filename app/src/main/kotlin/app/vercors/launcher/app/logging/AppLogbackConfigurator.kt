package app.vercors.launcher.app.logging

import app.vercors.launcher.core.storage.Storage
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.layout.TTLLLayout
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.Configurator.ExecutionStatus
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.spi.ContextAwareBase
import kotlinx.io.files.Path


class AppLogbackConfigurator : ContextAwareBase(), Configurator {
    init {
        instance = this
    }

    override fun configure(context: LoggerContext): ExecutionStatus {
        addInfo("Setting up custom configuration.")
        val logsPath = Path(Storage.instance.state.value.path, "logs")

        val layout = TTLLLayout().apply {
            this.context = context
            this.start()
        }

        val encoder = LayoutWrappingEncoder<ILoggingEvent>().apply {
            this.context = context
            this.layout = layout
        }

        val ca = ConsoleAppender<ILoggingEvent>().apply {
            this.context = context
            this.name = "console"
            this.encoder = encoder
            this.start()
        }

        val debugRollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
            this.context = context
            this.fileNamePattern = Path(logsPath, "debug.%d{yyyy-MM-dd}.log").toString()
            this.maxHistory = 7
        }

        val debugRfa = RollingFileAppender<ILoggingEvent>().apply {
            this.context = context
            this.name = "rollingFile"
            this.file = Path(logsPath, "debug.log").toString()
            this.encoder = encoder
            this.rollingPolicy = debugRollingPolicy.also {
                it.setParent(this)
                it.start()
            }
            this.start()
        }

        val infoFilter = ThresholdFilter().apply {
            this.context = context
            this.setLevel("INFO")
            this.start()
        }

        val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
            this.context = context
            this.fileNamePattern = Path(logsPath, "vercors.%d{yyyy-MM-dd}.log").toString()
            this.maxHistory = 7
        }

        val rfa = RollingFileAppender<ILoggingEvent>().apply {
            this.context = context
            this.name = "rollingFile"
            this.file = Path(logsPath, "vercors.log").toString()
            this.encoder = encoder
            this.rollingPolicy = rollingPolicy.also {
                it.setParent(this)
                it.start()
            }
            this.addFilter(infoFilter)
            this.start()
        }

        context.getLogger(Logger.ROOT_LOGGER_NAME).apply {
            level = Level.DEBUG
            addAppender(ca)
            addAppender(rfa)
            addAppender(debugRfa)
        }

        addInfo("Custom logger is set up")
        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
    }

    fun reload() {
        addInfo("Reloading custom logger.")
        val loggerContext = context as LoggerContext
        loggerContext.reset()
        instance.configure(loggerContext)
    }

    companion object {
        lateinit var instance: AppLogbackConfigurator
    }
}