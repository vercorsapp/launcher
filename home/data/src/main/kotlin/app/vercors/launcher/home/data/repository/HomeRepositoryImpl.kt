package app.vercors.launcher.home.data.repository

import app.vercors.launcher.home.domain.model.HomeSection
import app.vercors.launcher.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class HomeRepositoryImpl : HomeRepository {
    override fun observeSections(): Flow<List<HomeSection>> {
        TODO("Not yet implemented")
    }
}