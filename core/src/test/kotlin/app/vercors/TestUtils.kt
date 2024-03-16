package app.vercors

import app.vercors.common.AppComponentContext
import app.vercors.common.AppComponentContextImpl
import app.vercors.di.DI
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

fun TestScope.initMainDispatcher() = Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))

val EmptyDI = DI {}

fun componentContext(lifecycle: Lifecycle, di: DI = EmptyDI): AppComponentContext =
    AppComponentContextImpl(DefaultComponentContext(lifecycle), di)