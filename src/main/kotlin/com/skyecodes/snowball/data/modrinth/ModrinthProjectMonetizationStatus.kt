package com.skyecodes.snowball.data.modrinth

import com.skyecodes.snowball.data.StringEnumerable
import com.skyecodes.snowball.data.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ModrinthProjectMonetizationStatusSerializer::class)
enum class ModrinthProjectMonetizationStatus(override val value: String) : StringEnumerable {
    Monetized("monetized"),
    Demonetized("demonetized"),
    ForceDemonetized("force-demonetized")
}

private class ModrinthProjectMonetizationStatusSerializer :
    StringEnumerableSerializer<ModrinthProjectMonetizationStatus>(ModrinthProjectMonetizationStatus.entries)