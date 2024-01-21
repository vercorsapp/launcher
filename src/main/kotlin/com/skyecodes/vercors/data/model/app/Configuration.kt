package com.skyecodes.vercors.data.model.app

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val version: Int = 0,
    val theme: AppTheme = AppTheme.SYSTEM,
    val accentColor: AppColor = AppColor.Mauve,
    val useSystemWindowFrame: Boolean = false,
    val animations: Boolean = true,
    val defaultTab: AppTab = AppTab.Home,
    val homeSections: List<HomeSectionType> = HomeSectionType.entries,
    val homeProviders: List<Provider> = Provider.entries,
    val instancesSortOrder: InstancesSortOrder = InstancesSortOrder.LastPlayed
) {
    companion object {
        val DEFAULT = Configuration()
    }
}