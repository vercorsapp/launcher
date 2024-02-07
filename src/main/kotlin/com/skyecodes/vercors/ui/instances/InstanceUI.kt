package com.skyecodes.vercors.ui.instances

import androidx.compose.runtime.Stable
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.readable
import com.skyecodes.vercors.ui.Localization

val Instance.loaderAndVersionString: String @Stable get() = (loader?.value ?: Loader.Vanilla) + " " + gameVersion.id

fun Instance.lastPlayedString(locale: Localization): String =
    lastPlayed?.let { "${locale.lastPlayed} ${it.readable()}" } ?: locale.notPlayedBefore