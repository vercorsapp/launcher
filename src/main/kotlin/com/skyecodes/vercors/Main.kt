package com.skyecodes.vercors

import androidx.compose.ui.window.application
import com.skyecodes.vercors.data.model.app.Configuration
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.service.*
import com.skyecodes.vercors.logic.AppViewModel
import com.skyecodes.vercors.logic.HomeViewModel
import com.skyecodes.vercors.logic.InstancesViewModel
import com.skyecodes.vercors.logic.SettingsViewModel
import com.skyecodes.vercors.ui.AppWindow
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.stateholder.SavedStateHolder
import org.koin.compose.KoinApplication
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Vercors"

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Hello world! $APP_NAME v$APP_VERSION" }
    application {
        KoinApplication(application = {
            logger(SLF4JLogger())
            modules(module {
                single<ConfigurationService> { ConfigurationServiceImpl(get()) }
                single<CurseforgeService> { CurseforgeServiceImpl(get()) }
                single<HttpService> { HttpServiceImpl() }
                single<InstanceService> { InstanceServiceImpl(get()) }
                single<ModrinthService> { ModrinthServiceImpl(get()) }
                single<MojangService> { MojangServiceImpl(get()) }
                single<StorageService> { StorageServiceImpl() }

                factory { (navigator: Navigator) -> AppViewModel(get(), get(), navigator) }
                factory { (configuration: StateFlow<Configuration?>, instances: StateFlow<List<Instance>?>, savedStateHolder: SavedStateHolder) ->
                    HomeViewModel(get(), get(), configuration, instances, savedStateHolder)
                }
                factory { (instances: StateFlow<List<Instance>?>, openNewInstanceDialog: () -> Unit, closeNewInstanceDialog: () -> Unit) ->
                    InstancesViewModel(instances, openNewInstanceDialog, closeNewInstanceDialog)
                }
                factory { (onConfigChange: (Configuration) -> Unit) -> SettingsViewModel(onConfigChange) }
            })
        }) {
            PreComposeApp {
                val navigator = rememberNavigator()
                AppWindow(koinViewModel { parametersOf(navigator) })
            }
        }
    }
}
