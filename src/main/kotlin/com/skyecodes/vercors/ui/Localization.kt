package com.skyecodes.vercors.ui

import androidx.compose.runtime.compositionLocalOf

val LocalLocalization = compositionLocalOf<Localization> { Localization.En }

interface Localization {
    // General
    val home: String
    val instances: String
    val search: String
    val accounts: String
    val settings: String
    val cancel: String
    val close: String
    val ascending: String
    val descending: String
    val none: String
    val mods: String
    val modpacks: String
    val resourcePacks: String
    val shaderPacks: String

    // Home
    val jumpBackIn: String
    val popularMods: String
    val popularModpacks: String
    val popularResourcePacks: String
    val popularShaderPacks: String
    val notPlayedBefore: String
    val play: String
    val view: String
    val install: String

    // Instances
    val sortBy: String
    val lastPlayed: String
    val dateModified: String
    val dateCreated: String
    val gameVersion: String
    val name: String
    val groupBy: String
    val saveFilters: String
    val noInstancesFound: String
    val createNewInstance: String
    val instanceName: String
    val icon: String
    val minecraftVersion: String
    val includeSnapshots: String
    val loader: String
    val loaderVersion: String
    val create: String

    // Settings
    val userInterface: String
    val theme: String
    val themeDescription: String
    val system: String
    val light: String
    val dark: String
    val accentColor: String
    val accentColorDescription: String
    val systemWindow: String
    val systemWindowDescription: String
    val animations: String
    val animationsDescription: String
    val defaultTab: String
    val defaultTabDescription: String
    val sections: String
    val sectionsDescription: String
    val providers: String
    val providersDescription: String

    // Account
    val addAccount: String
    val addAccountLogin: String
    val addAccountInfo: String
    val openInBrowser: String
    val copyUrl: String
    val errorOccurred: String
    val microsoftAuth: String
    val microsoftToken: String
    val xboxLiveAuth: String
    val xstsToken: String
    val minecraftAuth: String
    val minecraftProfile: String

    object En : Localization {
        // General
        override val home = "Home"
        override val instances = "Instances"
        override val search = "Search"
        override val accounts = "Accounts"
        override val settings = "Settings"
        override val cancel = "Cancel"
        override val close = "Close"
        override val ascending = "Ascending order"
        override val descending = "Descending order"
        override val none = "None"

        // Home
        override val jumpBackIn = "Jump back in"
        override val popularMods = "Popular mods"
        override val popularModpacks = "Popular modpacks"
        override val popularResourcePacks = "Popular resource packs"
        override val popularShaderPacks = "Popular shader packs"
        override val mods = "Mods"
        override val modpacks = "Modpacks"
        override val resourcePacks = "Resource packs"
        override val shaderPacks = "Shader packs"
        override val notPlayedBefore = "Not played before"
        override val play = "Play"
        override val view = "View"
        override val install = "Install"

        // Instances
        override val sortBy = "Sort by"
        override val lastPlayed = "Last played"
        override val dateModified = "Date modified"
        override val dateCreated = "Date created"
        override val gameVersion = "Game version"
        override val name = "Name"
        override val groupBy = "Group by"
        override val saveFilters = "Save filters"
        override val noInstancesFound = "No instances found"
        override val createNewInstance = "Create new instance"
        override val instanceName = "Instance name"
        override val icon = "Icon"
        override val minecraftVersion = "Minecraft version"
        override val includeSnapshots = "Include snapshots"
        override val loader = "Loader"
        override val loaderVersion = "Loader version"
        override val create = "Create"

        // Settings
        override val userInterface = "User interface"
        override val theme = "Theme"
        override val system = "System"
        override val light = "Light"
        override val dark = "Dark"
        override val themeDescription = "Change the global theme of the launcher."
        override val accentColor = "Accent color"
        override val accentColorDescription = "Change the accent color used in some buttons."
        override val systemWindow = "Use system window frame"
        override val systemWindowDescription =
            "Use the system window frame instead of the custom window frame provided by the launcher."
        override val animations = "Animations"
        override val animationsDescription =
            "Enables UI animations such as fade in, smooth element size change, etc."
        override val defaultTab = "Default tab"
        override val defaultTabDescription = "Change the tab that shows up when the launcher is opened."
        override val sections = "Sections"
        override val sectionsDescription =
            "Choose which section(s) to show on the home tab."
        override val providers = "Providers"
        override val providersDescription =
            "Choose which service(s) provide popular projects on the home tab."

        // Account
        override val addAccount = "Add account"
        override val addAccountLogin =
            "Please log into the Microsoft account linked to your Minecraft profile through your browser."
        override val addAccountInfo = "This dialog window needs to stay open during the process."
        override val openInBrowser = "Open in browser"
        override val copyUrl = "Copy URL"
        override val errorOccurred = "An error has occured."
        override val microsoftAuth = "Awaiting authentication"
        override val microsoftToken = "Getting Microsoft access token"
        override val xboxLiveAuth = "Authenticating with Xbox Live"
        override val xstsToken = "Obtaining XSTS token"
        override val minecraftAuth = "Authenticating with Minecraft"
        override val minecraftProfile = "Getting Minecraft profile"
    }
}