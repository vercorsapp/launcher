package app.vercors.launcher.core.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.modifier.handPointer

@Composable
@Suppress("kotlin:S107")
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    icon: ImageVector,
    text: String,
) {
    Button(
        onClick = onClick,
        modifier = modifier.handPointer(enabled),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
@Suppress("kotlin:S107")
fun AppIconTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = null,
    icon: ImageVector,
    text: String,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.handPointer(enabled),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}