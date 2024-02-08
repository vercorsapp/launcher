package com.skyecodes.vercors.settings

import com.skyecodes.vercors.common.AppColor
import com.skyecodes.vercors.common.AppTheme
import com.skyecodes.vercors.home.HomeSectionType
import com.skyecodes.vercors.instances.InstanceSorter
import com.skyecodes.vercors.menu.AppTab
import com.skyecodes.vercors.projects.Provider
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val version: Int = 0,
    val theme: AppTheme = AppTheme.System,
    val accentColor: AppColor = AppColor.Mauve,
    val useSystemWindowFrame: Boolean = false,
    val animations: Boolean = true,
    val defaultTab: AppTab = AppTab.Home,
    val homeSections: List<HomeSectionType> = HomeSectionType.entries,
    val homeProviders: List<Provider> = Provider.entries,
    val savedInstanceSorter: InstanceSorter = InstanceSorter()
) {
    companion object {
        val DEFAULT = Configuration()
    }

}