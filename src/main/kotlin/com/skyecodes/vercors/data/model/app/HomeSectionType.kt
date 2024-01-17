package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(HomeSectionSerializer::class)
enum class HomeSectionType(val title: String, override val value: String) : StringEnumerable {
    JumpBackIn("Jump back in", "jumpBackIn"),
    PopularMods("Popular mods", "popularMods"),
    PopularModpacks("Popular modpacks", "popularModpacks"),
    PopularResourcePacks("Popular resource packs", "popularResourcePacks"),
    PopularShaderPacks("Popular shader packs", "popularShaderPacks");
}

private class HomeSectionSerializer : StringEnumerableSerializer<HomeSectionType>(HomeSectionType.entries)