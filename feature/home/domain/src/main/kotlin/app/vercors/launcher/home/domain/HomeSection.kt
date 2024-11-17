package app.vercors.launcher.home.domain

import app.vercors.launcher.instance.domain.Instance
import app.vercors.launcher.project.domain.Project

sealed interface HomeSection {
    val type: HomeSectionType

    data class Instances(override val type: HomeSectionType, val data: HomeSectionData<Instance>) : HomeSection
    data class Projects(override val type: HomeSectionType, val data: HomeSectionData<Project>) : HomeSection
}

sealed interface HomeSectionData<T> {
    class Loading<T> : HomeSectionData<T>

    @JvmInline
    value class Loaded<T>(val items: List<T>) : HomeSectionData<T>
}