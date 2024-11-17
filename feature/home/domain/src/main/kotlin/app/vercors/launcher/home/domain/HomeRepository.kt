package app.vercors.launcher.home.domain

import kotlinx.coroutines.flow.StateFlow

interface HomeRepository {
    val sectionsState: StateFlow<List<HomeSection>>
    suspend fun loadSections()
}
