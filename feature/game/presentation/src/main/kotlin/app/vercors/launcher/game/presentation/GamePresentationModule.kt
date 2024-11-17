package app.vercors.launcher.game.presentation

import app.vercors.launcher.game.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class GamePresentationModule

val GameString = Res.string
val GameDrawable = Res.drawable