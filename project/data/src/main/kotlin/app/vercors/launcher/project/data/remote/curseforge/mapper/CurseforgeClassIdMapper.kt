package app.vercors.launcher.project.data.remote.curseforge.mapper

import app.vercors.launcher.project.data.remote.curseforge.model.CurseforgeClassId
import app.vercors.launcher.project.data.remote.curseforge.model.CurseforgeClassIds
import app.vercors.launcher.project.domain.model.ProjectType

fun CurseforgeClassId?.toProjectType(): ProjectType = when (this) {
    CurseforgeClassIds.MODPACK -> ProjectType.Modpack
    CurseforgeClassIds.MOD -> ProjectType.Mod
    CurseforgeClassIds.RESOURCEPACK -> ProjectType.ResourcePack
    CurseforgeClassIds.SHADERPACK -> ProjectType.ShaderPack
    else -> throw IllegalArgumentException("Unknown Curseforge classId: $this")
}

fun ProjectType.toClassId(): CurseforgeClassId = when (this) {
    ProjectType.Mod -> CurseforgeClassIds.MOD
    ProjectType.Modpack -> CurseforgeClassIds.MODPACK
    ProjectType.ResourcePack -> CurseforgeClassIds.RESOURCEPACK
    ProjectType.ShaderPack -> CurseforgeClassIds.SHADERPACK
}