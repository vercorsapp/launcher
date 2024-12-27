package app.vercors.launcher.feature

import app.vercors.launcher.account.AccountFeatureModule
import app.vercors.launcher.account.SettingsFeatureModule
import app.vercors.launcher.game.GameFeatureModule
import app.vercors.launcher.home.HomeFeatureModule
import app.vercors.launcher.instance.InstanceFeatureModule
import app.vercors.launcher.project.ProjectFeatureModule
import app.vercors.launcher.setup.SetupFeatureModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        AccountFeatureModule::class,
        GameFeatureModule::class,
        HomeFeatureModule::class,
        InstanceFeatureModule::class,
        ProjectFeatureModule::class,
        SettingsFeatureModule::class,
        SetupFeatureModule::class,
    ]
)
class FeatureModule