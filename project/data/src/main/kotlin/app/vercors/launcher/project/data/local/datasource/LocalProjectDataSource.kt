package app.vercors.launcher.project.data.local.datasource

import app.vercors.launcher.project.data.local.dao.ProjectDao
import org.koin.core.annotation.Single

@Single
class LocalProjectDataSource(val dao: ProjectDao)