package app.vercors.launcher.home.domain.repository

import app.vercors.launcher.home.domain.model.HomeSection
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun observeSections(): Flow<List<HomeSection>>
}
