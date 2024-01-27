package com.skyecodes.vercors.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI

@Composable
fun AppTooltip(text: String) {
    Surface(
        color = LocalPalette.current.surface1,
        modifier = Modifier.shadow(4.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
        )
    }
}