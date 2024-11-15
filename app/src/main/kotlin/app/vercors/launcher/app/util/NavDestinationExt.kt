package app.vercors.launcher.app.util

import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.StringResource

@Deprecated("Needs to be changed to be more flexible")
typealias ScreenType = String

@Deprecated("Needs to be changed to be more flexible")
val NavDestination?.screenType: ScreenType?
    @Stable get() = this?.route?.substringAfterLast(".")?.substringBefore("?")

@Deprecated("Needs to be changed to be more flexible")
val ScreenType?.screenName: StringResource?
    @Stable get() = when (this) {
        "HomeRoute" -> CoreString.home
        "InstanceListRoute" -> CoreString.instances
        "ProjectListRoute" -> CoreString.projects
        "AccountListRoute" -> CoreString.accounts
        "SettingsRoute" -> CoreString.settings
        else -> null
    }
