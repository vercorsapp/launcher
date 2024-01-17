package com.skyecodes.vercors.data.model.mojang


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MojangVersionInfo(
    val arguments: Arguments,
    val assetIndex: AssetIndex,
    val assets: String,
    val complianceLevel: Int,
    val downloads: Downloads,
    val id: String,
    val javaVersion: JavaVersion,
    val libraries: List<Library>,
    val logging: Logging,
    val mainClass: String,
    val minimumLauncherVersion: Int,
    val releaseTime: MojangInstant,
    val time: MojangInstant,
    val type: String
) {
    @Serializable
    data class Arguments(
        val game: List<MojangArgument>,
        val jvm: List<MojangArgument>
    )

    @Serializable
    data class AssetIndex(
        val id: String,
        override val sha1: String,
        override val size: Int,
        val totalSize: Int,
        override val url: String
    ) : MojangFile

    @Serializable
    data class Downloads(
        val client: Download,
        @SerialName("client_mappings")
        val clientMappings: Download,
        val server: Download,
        @SerialName("server_mappings")
        val serverMappings: Download
    ) {
        @Serializable
        data class Download(
            override val sha1: String,
            override val size: Int,
            override val url: String
        ) : MojangFile
    }

    @Serializable
    data class JavaVersion(
        val component: String,
        val majorVersion: Int
    )

    @Serializable
    data class Library(
        val downloads: Downloads,
        val name: String,
        val rules: List<Rule>? = null
    ) {
        @Serializable
        data class Downloads(
            val artifact: Artifact
        ) {
            @Serializable
            data class Artifact(
                val path: String,
                override val sha1: String,
                override val size: Int,
                override val url: String
            ) : MojangFile
        }
    }

    @Serializable
    data class Logging(
        val client: Client
    ) {
        @Serializable
        data class Client(
            val argument: String,
            val file: File,
            val type: String
        ) {
            @Serializable
            data class File(
                val id: String,
                override val sha1: String,
                override val size: Int,
                override val url: String
            ) : MojangFile
        }
    }

    @Serializable
    data class Rule(
        val action: String,
        val os: Os? = null,
        val features: Map<String, Boolean>? = null
    ) {
        @Serializable
        data class Os(
            val name: String? = null,
            val arch: String? = null
        )
    }
}