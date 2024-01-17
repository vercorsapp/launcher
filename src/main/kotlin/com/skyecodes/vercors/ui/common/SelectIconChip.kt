package com.skyecodes.vercors.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.ui.UI

@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    imageVector: ImageVector
) {
    SelectIconChip(
        selected = selected,
        onClick = onClick,
        text = text
    ) {
        Icon(imageVector, null, Modifier.size(UI.mediumIconSize))
    }
}

@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    painter: Painter
) {
    SelectIconChip(
        selected = selected,
        onClick = onClick,
        text = text
    ) {
        Icon(painter, null, Modifier.size(UI.mediumIconSize))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    icon: @Composable () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        trailingIcon = icon,
        colors = ChipDefaults.filterChipColors(
            backgroundColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        )
    ) {
        Text(text, style = MaterialTheme.typography.body1)
    }
}
