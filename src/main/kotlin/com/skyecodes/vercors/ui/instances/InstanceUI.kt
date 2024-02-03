package com.skyecodes.vercors.ui.instances

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.data.model.app.Loader
import com.skyecodes.vercors.readable
import com.skyecodes.vercors.ui.LocalLocalization

val Instance.loaderAndVersionString: String @Stable get() = (loader?.value ?: Loader.Vanilla) + " " + gameVersion.id

val Instance.lastPlayedString: String
    @Composable get() =
        lastPlayed?.let { "${LocalLocalization.current.lastPlayed} ${it.readable()}" }
            ?: LocalLocalization.current.notPlayedBefore