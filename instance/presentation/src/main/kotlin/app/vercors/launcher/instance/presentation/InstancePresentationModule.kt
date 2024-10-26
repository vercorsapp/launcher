package app.vercors.launcher.instance.presentation

import app.vercors.launcher.instance.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class InstancePresentationModule

val InstanceString = Res.string
val InstanceDrawable = Res.drawable