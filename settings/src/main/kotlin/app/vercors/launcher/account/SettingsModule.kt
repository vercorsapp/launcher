package app.vercors.launcher.account

import app.vercors.launcher.settings.data.SettingsDataModule
import app.vercors.launcher.settings.domain.SettingsDomainModule
import app.vercors.launcher.settings.presentation.SettingsPresentationModule
import org.koin.core.annotation.Module

@Module(includes = [SettingsDataModule::class, SettingsDomainModule::class, SettingsPresentationModule::class])
class SettingsModule