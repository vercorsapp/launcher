package app.vercors.launcher.project

import app.vercors.launcher.project.data.ProjectDataModule
import app.vercors.launcher.project.domain.ProjectDomainModule
import app.vercors.launcher.project.presentation.ProjectPresentationModule
import org.koin.core.annotation.Module

@Module(includes = [ProjectDataModule::class, ProjectDomainModule::class, ProjectPresentationModule::class])
class ProjectFeatureModule