package app.vercors.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortOrder(val value: String) {
    @SerialName("asc")
    Asc("asc"),

    @SerialName("desc")
    Desc("desc");

    val opposite by lazy {
        when (this) {
            Asc -> Desc
            Desc -> Asc
        }
    }
}
