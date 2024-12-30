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

package app.vercors.meta.project.modrinth


import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ModrinthProjectResult(
    val author: String,
    val categories: List<String>? = null,
    @SerialName("client_side")
    val clientSide: ModrinthProjectSideSupport,
    val color: Int? = null,
    @SerialName("date_created")
    @Contextual
    val dateCreated: Instant,
    @SerialName("date_modified")
    @Contextual
    val dateModified: Instant,
    val description: String,
    @SerialName("display_categories")
    val displayCategories: List<String>? = null,
    val downloads: Long,
    @SerialName("featured_gallery")
    val featuredGallery: String? = null,
    val follows: Int,
    val gallery: List<String>? = null,
    @SerialName("icon_url")
    val iconUrl: String? = null,
    @SerialName("latest_version")
    val latestVersion: String? = null,
    val license: String,
    @SerialName("monetization_status")
    val monetizationStatus: ModrinthProjectMonetizationStatus? = null,
    @SerialName("project_id")
    val projectId: String,
    @SerialName("project_type")
    val projectType: ModrinthProjectType,
    @SerialName("server_side")
    val serverSide: ModrinthProjectSideSupport,
    val slug: String,
    @SerialName("thread_id")
    val threadId: String? = null,
    val title: String,
    val versions: List<String>
)