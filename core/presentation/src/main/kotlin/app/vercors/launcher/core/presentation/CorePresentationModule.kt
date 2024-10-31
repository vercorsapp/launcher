package app.vercors.launcher.core.presentation

import app.vercors.launcher.core.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class CorePresentationModule

val CoreString = Res.string
val CoreDrawable = Res.drawable