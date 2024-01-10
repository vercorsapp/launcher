package com.skyecodes.snowball.ui

import androidx.compose.runtime.compositionLocalOf
import com.skyecodes.snowball.data.app.Configuration
import com.skyecodes.snowball.data.app.Instance

object Data {
    val configuration = compositionLocalOf { Configuration.DEFAULT }
    val instances = compositionLocalOf { emptyList<Instance>() }
}