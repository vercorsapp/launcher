package app.vercors.launcher.instance

import app.vercors.launcher.instance.data.InstanceDataModule
import app.vercors.launcher.instance.domain.InstanceDomainModule
import app.vercors.launcher.instance.presentation.InstancePresentationModule
import org.koin.core.annotation.Module

@Module(includes = [InstanceDataModule::class, InstanceDomainModule::class, InstancePresentationModule::class])
class InstanceModule