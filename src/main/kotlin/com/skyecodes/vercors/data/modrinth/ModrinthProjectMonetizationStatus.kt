package com.skyecodes.vercors.data.modrinth

import com.skyecodes.vercors.data.StringEnumerable
import com.skyecodes.vercors.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ModrinthProjectMonetizationStatusSerializer::class)
enum class ModrinthProjectMonetizationStatus(override val value: String) : StringEnumerable {
    Monetized("monetized"),
    Demonetized("demonetized"),
    ForceDemonetized("force-demonetized")
}

private class ModrinthProjectMonetizationStatusSerializer :
    StringEnumerableSerializer<ModrinthProjectMonetizationStatus>(ModrinthProjectMonetizationStatus.entries)