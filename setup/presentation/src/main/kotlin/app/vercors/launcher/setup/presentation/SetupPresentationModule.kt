package app.vercors.launcher.setup.presentation

import app.vercors.launcher.setup.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class SetupPresentationModule

val SetupString = Res.string
val SetupDrawable = Res.drawable