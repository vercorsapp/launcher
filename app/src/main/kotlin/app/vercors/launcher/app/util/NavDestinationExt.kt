package app.vercors.launcher.app.util

import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import app.vercors.launcher.core.resources.*
import org.jetbrains.compose.resources.StringResource

@Deprecated("Needs to be changed to be more flexible")
typealias ScreenType = String

@Deprecated("Needs to be changed to be more flexible")
val NavDestination?.screenType: ScreenType?
    @Stable get() = this?.route?.substringAfterLast(".")?.substringBefore("?")

@Deprecated("Needs to be changed to be more flexible")
val ScreenType?.screenName: StringResource?
    @Stable get() = when (this) {
        "HomeRoute" -> appString { home }
        "InstanceListRoute" -> appString { instances }
        "ProjectListRoute" -> appString { projects }
        "AccountListRoute" -> appString { accounts }
        "SettingsRoute" -> appString { settings }
        else -> null
    }
