package app.vercors.launcher.account

import app.vercors.launcher.settings.presentation.SettingsPresentationModule
import org.koin.core.annotation.Module

@Module(includes = [SettingsPresentationModule::class])
class SettingsFeatureModule