package app.vercors.launcher.core

import app.vercors.launcher.core.presentation.navigation.AppDestination
import app.vercors.launcher.core.presentation.navigation.DefaultNavigator
import app.vercors.launcher.core.presentation.navigation.Navigator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class CoreModule

@Single
@Named("mainDispatcher")
fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

@Single
@Named("ioDispatcher")
fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

@Single
@Named("defaultDispatcher")
fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

@Single
fun provideNavigator(): Navigator = DefaultNavigator(AppDestination.Home)