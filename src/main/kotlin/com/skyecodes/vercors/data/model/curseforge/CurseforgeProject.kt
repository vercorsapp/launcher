package com.skyecodes.vercors.data.model.curseforge

import kotlinx.serialization.Serializable

@Serializable
data class CurseforgeProject(
    val allowModDistribution: Boolean? = null,
    val authors: List<Author>,
    val categories: List<CurseforgeCategory>,
    val classId: Int? = null,
    val dateCreated: CurseforgeInstant,
    val dateModified: CurseforgeInstant,
    val dateReleased: CurseforgeInstant,
    val downloadCount: Long,
    val gameId: Int,
    val gamePopularityRank: Int,
    val id: Int,
    val isAvailable: Boolean,
    val isFeatured: Boolean,
    val latestEarlyAccessFilesIndexes: List<FileIndex>,
    val latestFiles: List<CurseforgeFile>,
    val latestFilesIndexes: List<FileIndex>,
    val links: ModLinks,
    val logo: ModAsset,
    val mainFileId: Int,
    val name: String,
    val primaryCategoryId: Int,
    val rating: Double? = null,
    val screenshots: List<ModAsset>,
    val slug: String,
    val status: CurseforgeProjectStatus,
    val summary: String,
    val thumbsUpCount: Int
) {

    @Serializable
    data class Author(
        val id: Int,
        val name: String,
        val url: String
    )

    @Serializable
    data class FileIndex(
        val fileId: Int,
        val filename: String,
        val gameVersion: String,
        val gameVersionTypeId: Int? = null,
        val modLoader: CurseforgeModLoaderType? = null,
        val releaseType: CurseforgeFileReleaseType
    )

    @Serializable
    data class ModLinks(
        val issuesUrl: String? = null,
        val sourceUrl: String? = null,
        val websiteUrl: String,
        val wikiUrl: String? = null
    )

    @Serializable
    data class ModAsset(
        val description: String,
        val id: Int,
        val modId: Int,
        val thumbnailUrl: String,
        val title: String,
        val url: String
    )
}