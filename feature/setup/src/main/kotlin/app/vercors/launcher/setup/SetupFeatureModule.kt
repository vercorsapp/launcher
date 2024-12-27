package app.vercors.launcher.setup

import app.vercors.launcher.setup.data.SetupDataModule
import app.vercors.launcher.setup.domain.SetupDomainModule
import app.vercors.launcher.setup.presentation.SetupPresentationModule
import org.koin.core.annotation.Module

@Module(includes = [SetupDataModule::class, SetupDomainModule::class, SetupPresentationModule::class])
class SetupFeatureModule