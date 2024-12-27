package app.vercors.launcher.app

import app.vercors.launcher.core.CoreModule
import app.vercors.launcher.feature.FeatureModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreModule::class,
        FeatureModule::class,
    ]
)
@ComponentScan
class AppModule
