package app.vercors.launcher.core

import app.vercors.launcher.core.config.CoreConfigModule
import app.vercors.launcher.core.domain.CoreDomainModule
import app.vercors.launcher.core.network.CoreNetworkModule
import app.vercors.launcher.core.presentation.CorePresentationModule
import app.vercors.launcher.core.serialization.CoreSerializationModule
import app.vercors.launcher.core.storage.CoreStorageModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreConfigModule::class,
        CoreDomainModule::class,
        CoreNetworkModule::class,
        CorePresentationModule::class,
        CoreSerializationModule::class,
        CoreStorageModule::class
    ]
)
class CoreModule