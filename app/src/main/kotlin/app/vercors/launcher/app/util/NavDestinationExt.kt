package app.vercors.launcher.app.util

import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import app.vercors.launcher.core.generated.resources.*
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.StringResource

typealias ScreenType = String

val NavDestination?.screenType: ScreenType?
    @Stable get() = this?.route?.substringAfterLast(".")?.substringBefore("?")

val ScreenType?.screenName: StringResource?
    @Stable get() = when (this) {
        "Home" -> CoreString.home
        "InstanceList" -> CoreString.instances
        "ProjectList" -> CoreString.projects
        "Accounts" -> CoreString.accounts
        "Settings" -> CoreString.settings
        else -> null
    }
