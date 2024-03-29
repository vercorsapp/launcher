package app.vercors.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import app.vercors.UI

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

@Composable
fun SelectIconChip(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    icon: (@Composable () -> Unit)? = null
) {
    val backgroundColor by appAnimateColorAsState(selected, MaterialTheme.colors.primary, MaterialTheme.colors.surface)
    val contentColor by appAnimateColorAsState(selected, MaterialTheme.colors.onPrimary, MaterialTheme.colors.onSurface)

    FilterChip(
        selected = selected,
        onClick = onClick,
        trailingIcon = icon,
        colors = ChipDefaults.filterChipColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
    ) {
        Text(text, style = MaterialTheme.typography.button)
    }
}
