package com.skyecodes.vercors.component

import com.arkivanov.essenty.lifecycle.subscribe
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

abstract class AbstractComponent(componentContext: AppComponentContext) : AppComponentContext by componentContext {
    init {
        lifecycle.subscribe(
            onCreate = { logger.trace { "$this -> ${lifecycle.state}" } },
            onStart = { logger.trace { "$this -> ${lifecycle.state}" } },
            onResume = { logger.trace { "$this -> ${lifecycle.state}" } },
            onPause = { logger.trace { "$this -> ${lifecycle.state}" } },
            onStop = { logger.trace { "$this -> ${lifecycle.state}" } },
            onDestroy = { logger.trace { "$this -> ${lifecycle.state}" } },
        )
    }
}