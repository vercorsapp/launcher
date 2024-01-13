package com.skyecodes.vercors.ui

import androidx.compose.runtime.compositionLocalOf
import com.skyecodes.vercors.data.app.Configuration
import com.skyecodes.vercors.data.app.Instance

object Data {
    val configuration = compositionLocalOf { Configuration.DEFAULT }
    val instances = compositionLocalOf { emptyList<Instance>() }
}