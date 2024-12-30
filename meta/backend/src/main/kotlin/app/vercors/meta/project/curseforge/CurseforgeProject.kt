/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package app.vercors.meta.project.curseforge

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CurseforgeProject(
    val allowModDistribution: Boolean? = null,
    val authors: List<Author>,
    val categories: List<CurseforgeCategory>,
    val classId: Int? = null,
    @Contextual
    val dateCreated: Instant,
    @Contextual
    val dateModified: Instant,
    @Contextual
    val dateReleased: Instant,
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