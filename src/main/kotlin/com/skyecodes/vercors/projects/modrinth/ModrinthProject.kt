package com.skyecodes.vercors.projects.modrinth


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
    val license: ModrinthLicense,
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