package app.vercors.launcher.home.domain.repository

import app.vercors.launcher.home.domain.model.HomeSection
import kotlinx.coroutines.flow.StateFlow

interface HomeRepository {
    val sectionsState: StateFlow<List<HomeSection>>
    suspend fun loadSections()
}
