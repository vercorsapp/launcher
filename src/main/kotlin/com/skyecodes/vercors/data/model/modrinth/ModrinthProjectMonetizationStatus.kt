package com.skyecodes.vercors.data.model.modrinth

import com.skyecodes.vercors.data.model.StringEnumerable
import com.skyecodes.vercors.data.model.StringEnumerableSerializer
import kotlinx.serialization.Serializable

@Serializable(ModrinthProjectMonetizationStatusSerializer::class)
enum class ModrinthProjectMonetizationStatus(override val value: String) : StringEnumerable {
    Monetized("monetized"),
    Demonetized("demonetized"),
    ForceDemonetized("force-demonetized")
}

private class ModrinthProjectMonetizationStatusSerializer :
    StringEnumerableSerializer<ModrinthProjectMonetizationStatus>(ModrinthProjectMonetizationStatus.entries)