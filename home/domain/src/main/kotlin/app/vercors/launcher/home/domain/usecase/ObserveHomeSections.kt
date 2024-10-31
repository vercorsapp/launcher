package app.vercors.launcher.home.domain.usecase

import app.vercors.launcher.home.domain.model.HomeSection
import app.vercors.launcher.home.domain.repository.HomeRepository
import app.vercors.launcher.instance.domain.repository.InstanceRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ObserveHomeSections(
    private val homeRepository: HomeRepository,
    private val instanceRepository: InstanceRepository
) {
    operator fun invoke(): Flow<List<HomeSection>> {
        TODO()
    }
}