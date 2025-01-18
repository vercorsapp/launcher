/*
 * Copyright (c) 2024-2025 skyecodes
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

package app.vercors.lib.platform.curseforge

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CurseForgeFile(
    val alternateFileId: Int? = null,
    val dependencies: List<FileDependency>,
    val displayName: String,
    val downloadCount: Long,
    val downloadUrl: String? = null,
    @Contextual
    val earlyAccessEndDate: CurseForgeInstant? = null,
    val exposeAsAlternative: Boolean? = null,
    @Contextual
    val fileDate: CurseForgeInstant,
    val fileFingerprint: Long,
    val fileLength: Long,
    val fileName: String,
    val fileSizeOnDisk: Long? = null,
    val fileStatus: CurseForgeFileStatus,
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
    val releaseType: CurseForgeFileReleaseType,
    val serverPackFileId: Int? = null,
    val sortableGameVersions: List<SortableGameVersion>
) {
    @Serializable
    data class FileDependency(
        val modId: Int,
        val relationType: CurseForgeFileRelationType
    )

    @Serializable
    data class FileHash(
        val algo: CurseForgeHashAlgo,
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
        @Contextual
        val gameVersionReleaseDate: CurseForgeInstant,
        val gameVersionTypeId: Int? = null
    )
}