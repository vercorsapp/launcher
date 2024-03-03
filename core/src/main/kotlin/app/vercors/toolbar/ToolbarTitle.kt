package app.vercors.toolbar

sealed interface ToolbarTitle {
    data object Home : ToolbarTitle
    data object Instances : ToolbarTitle
    data object Search : ToolbarTitle
    data object Settings : ToolbarTitle
    data object InstanceInfo : ToolbarTitle
}
