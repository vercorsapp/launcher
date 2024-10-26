package app.vercors.launcher.home.domain.usecase

import app.vercors.launcher.home.domain.model.HomeSection
import app.vercors.launcher.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ObserveHomeSections(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<HomeSection>> {
        TODO()
    }
}