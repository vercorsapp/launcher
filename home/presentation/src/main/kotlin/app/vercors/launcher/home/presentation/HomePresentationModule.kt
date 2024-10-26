package app.vercors.launcher.home.presentation

import app.vercors.launcher.home.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class HomePresentationModule

val HomeString = Res.string
val HomeDrawable = Res.drawable