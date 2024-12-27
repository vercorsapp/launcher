package app.vercors.launcher.home

import app.vercors.launcher.home.data.HomeDataModule
import app.vercors.launcher.home.domain.HomeDomainModule
import app.vercors.launcher.home.presentation.HomePresentationModule
import org.koin.core.annotation.Module

@Module(includes = [HomeDataModule::class, HomeDomainModule::class, HomePresentationModule::class])
class HomeFeatureModule