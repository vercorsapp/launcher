package com.skyecodes.vercors.projects.modrinth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProjectResult(
    val author: String,
    val categories: List<String>? = null,
    @SerialName("client_side")
    val clientSide: ModrinthProjectSideSupport,
    val color: Int? = null,
    @SerialName("date_created")
    val dateCreated: ModrinthInstant,
    @SerialName("date_modified")
    val dateModified: ModrinthInstant,
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