package app.vercors.launcher.settings.presentation

import app.vercors.launcher.settings.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class SettingsPresentationModule

val SettingsString = Res.string
val SettingsDrawable = Res.drawable