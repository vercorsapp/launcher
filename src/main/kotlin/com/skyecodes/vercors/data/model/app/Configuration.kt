package com.skyecodes.vercors.data.model.app

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val version: Int = 0,
    val theme: AppTheme = AppTheme.SYSTEM,
    val useSystemWindowFrame: Boolean = false,
    val defaultScene: AppScene = AppScene.Home,
    val homeSections: List<HomeSectionType> = HomeSectionType.entries,
    val homeProviders: List<Provider> = Provider.entries
) {
    companion object {
        val DEFAULT = Configuration()
    }
}