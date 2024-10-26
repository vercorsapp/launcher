package app.vercors.launcher.app

import app.vercors.launcher.account.AccountModule
import app.vercors.launcher.app.generated.resources.Res
import app.vercors.launcher.core.CoreModule
import app.vercors.launcher.game.GameModule
import app.vercors.launcher.home.HomeModule
import app.vercors.launcher.instance.InstanceModule
import app.vercors.launcher.project.ProjectModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        AccountModule::class,
        CoreModule::class,
        GameModule::class,
        HomeModule::class,
        InstanceModule::class,
        ProjectModule::class
    ]
)
@ComponentScan
class AppModule

val AppString = Res.string
val AppDrawable = Res.drawable
val AppFont = Res.font