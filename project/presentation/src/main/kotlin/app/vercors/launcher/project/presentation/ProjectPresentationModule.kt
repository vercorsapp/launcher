package app.vercors.launcher.project.presentation

import app.vercors.launcher.project.generated.resources.Res
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class ProjectPresentationModule

val ProjectString = Res.string
val ProjectDrawable = Res.drawable