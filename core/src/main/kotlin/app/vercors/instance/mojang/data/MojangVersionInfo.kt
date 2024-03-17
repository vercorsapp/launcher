/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.instance.mojang.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MojangVersionInfo(
    val arguments: Arguments?,
    val assetIndex: AssetIndex,
    val assets: String,
    val complianceLevel: Int,
    val downloads: Downloads,
    val id: String,
    val javaVersion: JavaVersion,
    val libraries: List<Library>,
    val logging: Logging?,
    val mainClass: String,
    val minecraftArguments: String?,
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
        val clientMappings: Download?,
        val server: Download?,
        @SerialName("server_mappings")
        val serverMappings: Download?
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
        val extract: Extract?,
        val name: String,
        val natives: Map<String, String>?,
        val rules: List<Rule>? = null
    ) {
        @Serializable
        data class Downloads(
            val artifact: Artifact?,
            val classifiers: Map<String, Artifact>?
        ) {
            @Serializable
            data class Artifact(
                val path: String,
                override val sha1: String,
                override val size: Int,
                override val url: String
            ) : MojangFile
        }

        @Serializable
        data class Extract(
            val exclude: List<String>
        )
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
            val version: String? = null,
            val arch: String? = null
        )
    }
}