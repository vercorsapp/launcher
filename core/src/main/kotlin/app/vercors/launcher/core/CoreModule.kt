package app.vercors.launcher.core

import app.vercors.launcher.core.data.CoreDataModule
import app.vercors.launcher.core.domain.CoreDomainModule
import app.vercors.launcher.core.presentation.CorePresentationModule
import org.koin.core.annotation.Module

@Module(includes = [CoreDataModule::class, CoreDomainModule::class, CorePresentationModule::class])
class CoreModule