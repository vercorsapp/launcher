package app.vercors.launcher.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class CoreDomainModule

@Single
@Named("mainDispatcher")
fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

@Single
@Named("ioDispatcher")
fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

@Single
@Named("defaultDispatcher")
fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default