package com.skyecodes.vercors.instances

import androidx.compose.runtime.Stable
import com.skyecodes.vercors.common.Localization
import com.skyecodes.vercors.projects.Loader
import com.skyecodes.vercors.readable

val Instance.loaderAndVersionString: String @Stable get() = (loader?.value ?: Loader.Vanilla) + " " + gameVersion.id

fun Instance.lastPlayedString(locale: Localization): String =
    lastPlayed?.let { "${locale.lastPlayed} ${it.readable()}" } ?: locale.notPlayedBefore