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


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProject(
    @SerialName("additional_categories")
    val additionalCategories: List<String>,
    val approved: String,
    val body: String,
    val categories: List<String>,
    @SerialName("client_side")
    val clientSide: String,
    val color: Int,
    val description: String,
    @SerialName("discord_url")
    val discordUrl: String,
    @SerialName("donation_urls")
    val donationUrls: List<ModrinthProjectDonationUrl>,
    val downloads: Int,
    val followers: Int,
    val gallery: List<ModrinthProjectGallery>,
    @SerialName("game_versions")
    val gameVersions: List<String>,
    @SerialName("icon_url")
    val iconUrl: String,
    val id: String,
    @SerialName("issues_url")
    val issuesUrl: String,
    val license: ModrinthProjectLicense,
    val loaders: List<String>,
    @SerialName("monetization_status")
    val monetizationStatus: String,
    @SerialName("project_type")
    val projectType: String,
    val published: String,
    val queued: String,
    @SerialName("requested_status")
    val requestedStatus: String,
    @SerialName("server_side")
    val serverSide: String,
    val slug: String,
    @SerialName("source_url")
    val sourceUrl: String,
    val status: String,
    val team: String,
    @SerialName("thread_id")
    val threadId: String,
    val title: String,
    val updated: String,
    val versions: List<String>,
    @SerialName("wiki_url")
    val wikiUrl: String
)