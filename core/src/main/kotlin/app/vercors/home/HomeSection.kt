package app.vercors.home

import app.vercors.instance.InstanceData
import app.vercors.project.ProjectData

sealed class HomeSection(val type: HomeSectionType) {
    data class Instances(val instances: List<InstanceData>? = null) : HomeSection(HomeSectionType.JumpBackIn)
    class Projects(type: HomeSectionType, val projects: List<ProjectData>? = null) : HomeSection(type)
}
