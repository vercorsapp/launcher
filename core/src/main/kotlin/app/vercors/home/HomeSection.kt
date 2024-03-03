package app.vercors.home

import app.vercors.instance.InstanceData
import app.vercors.project.ProjectData
import kotlinx.collections.immutable.ImmutableList

sealed class HomeSection(val type: HomeSectionType) {
    data class Instances(val instances: ImmutableList<InstanceData>? = null) : HomeSection(HomeSectionType.JumpBackIn)
    class Projects(type: HomeSectionType, val projects: ImmutableList<ProjectData>? = null) : HomeSection(type)
}
