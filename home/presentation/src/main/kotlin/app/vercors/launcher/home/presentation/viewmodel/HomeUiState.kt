package app.vercors.launcher.home.presentation.viewmodel

import androidx.compose.runtime.Immutable
import app.vercors.launcher.instance.domain.model.InstanceId
import app.vercors.launcher.project.domain.model.ProjectId
import org.jetbrains.compose.resources.StringResource
import java.time.Instant

@JvmInline
@Immutable
value class HomeUiState(
    val sections: List<HomeSectionUi> = emptyList(),
)

sealed interface HomeSectionUi {
    val title: StringResource

    data class Instances(override val title: StringResource, val data: HomeSectionDataUi<HomeSectionItemUi.Instance>) :
        HomeSectionUi

    data class Projects(override val title: StringResource, val data: HomeSectionDataUi<HomeSectionItemUi.Project>) :
        HomeSectionUi
}

sealed interface HomeSectionDataUi<T> {
    class Loading<T> : HomeSectionDataUi<T>

    @JvmInline
    value class Loaded<T>(val items: List<T>) : HomeSectionDataUi<T>
}

sealed interface HomeSectionItemUi {
    data class Instance(
        val id: InstanceId,
        val name: String,
        val loader: String,
        val gameVersion: String,
        val status: HomeInstanceStatusUi
    ) : HomeSectionItemUi {
        val loaderAndGameVersion = "$loader $gameVersion"
    }

    data class Project(
        val id: ProjectId,
        val name: String,
        val author: String,
        val iconUrl: String?,
        val imageUrl: String?,
        val description: String,
        val downloadCount: String,
        val lastUpdated: Instant
    ) : HomeSectionItemUi

}

sealed interface HomeInstanceStatusUi {
    @JvmInline
    value class NotRunning(val lastPlayed: String) : HomeInstanceStatusUi
    data object Running : HomeInstanceStatusUi
}
