package app.vercors.launcher.project.data.remote.modrinth.mapper

import app.vercors.launcher.project.data.remote.modrinth.model.ModrinthProjectType
import app.vercors.launcher.project.domain.model.ProjectType

fun ModrinthProjectType.toProjectType(): ProjectType = when (this) {
    ModrinthProjectType.Mod -> ProjectType.Mod
    ModrinthProjectType.Modpack -> ProjectType.Modpack
    ModrinthProjectType.ResourcePack -> ProjectType.ResourcePack
    ModrinthProjectType.ShaderPack -> ProjectType.ShaderPack
}

fun ProjectType.toModrinthProjectType(): ModrinthProjectType = when (this) {
    ProjectType.Mod -> ModrinthProjectType.Mod
    ProjectType.Modpack -> ModrinthProjectType.Modpack
    ProjectType.ResourcePack -> ModrinthProjectType.ResourcePack
    ProjectType.ShaderPack -> ModrinthProjectType.ShaderPack
}