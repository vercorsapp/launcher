package app.vercors.launcher.core.storage

import app.vercors.launcher.core.domain.APP_NAME
import ca.gosyer.appdirs.AppDirs
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class CoreStorageModule

@Single
fun provideAppDirs(): AppDirs = AppDirs(APP_NAME)