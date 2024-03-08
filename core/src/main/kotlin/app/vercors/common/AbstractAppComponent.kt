package app.vercors.common

import com.arkivanov.essenty.lifecycle.subscribe
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

abstract class AbstractAppComponent(
    componentContext: AppComponentContext
) : AppComponentContext by componentContext {
    init {
        componentContext.lifecycle.subscribe(
            onCreate = ::onCreate,
            onStart = ::onStart,
            onResume = ::onResume,
            onPause = ::onPause,
            onStop = ::onStop,
            onDestroy = ::onDestroy
        )
    }

    open fun onCreate() {
        log("onCreate")
    }

    open fun onStart() {
        log("onStart")
    }

    open fun onResume() {
        log("onResume")
    }

    open fun onPause() {
        log("onPause")
    }

    open fun onStop() {
        log("onStop")
    }

    open fun onDestroy() {
        log("onDestroy")
    }

    private fun log(state: String) {
        logger.trace { "$this -> $state" }
    }
}