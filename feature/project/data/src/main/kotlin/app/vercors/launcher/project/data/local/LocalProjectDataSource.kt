package app.vercors.launcher.project.data.local

import org.koin.core.annotation.Single

@Single
class LocalProjectDataSource(val dao: ProjectDao)