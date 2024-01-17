package com.skyecodes.vercors.data.model.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeFile(
    val alternateFileId: Int? = null,
    val dependencies: List<FileDependency>,
    val displayName: String,
    val downloadCount: Long,
    val downloadUrl: String? = null,
    val earlyAccessEndDate: CurseforgeInstant? = null,
    val exposeAsAlternative: Boolean? = null,
    val fileDate: CurseforgeInstant,
    val fileFingerprint: Long,
    val fileLength: Long,
    val fileName: String,
    val fileSizeOnDisk: Long? = null,
    val fileStatus: CurseforgeFileStatus,
    val gameId: Int,
    val gameVersions: List<String>,
    val hashes: List<FileHash>,
    val id: Int,
    val isAvailable: Boolean,
    val isEarlyAccessContent: Boolean? = null,
    val isServerPack: Boolean? = null,
    val modId: Int,
    val modules: List<FileModule>,
    val parentProjectFileId: Int? = null,
    val releaseType: CurseforgeFileReleaseType,
    val serverPackFileId: Int? = null,
    val sortableGameVersions: List<SortableGameVersion>
) {
    @Serializable
    data class FileDependency(
        val modId: Int,
        val relationType: CurseforgeFileRelationType
    )

    @Serializable
    data class FileHash(
        val algo: CurseforgeHashAlgo,
        val value: String
    )

    @Serializable
    data class FileModule(
        val fingerprint: Long,
        val name: String
    )

    @Serializable
    data class SortableGameVersion(
        val gameVersion: String,
        val gameVersionName: String,
        val gameVersionPadded: String,
        val gameVersionReleaseDate: CurseforgeInstant,
        val gameVersionTypeId: Int? = null
    )
}