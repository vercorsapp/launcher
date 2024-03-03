package app.vercors.home

import app.vercors.project.ProjectData

interface HomeProvider {
    suspend fun getPopularMods(): List<ProjectData>
    suspend fun getPopularModpacks(): List<ProjectData>
    suspend fun getPopularResourcePacks(): List<ProjectData>
    suspend fun getPopularShaderPacks(): List<ProjectData>
}