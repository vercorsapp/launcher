package com.skyecodes.vercors.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import com.skyecodes.vercors.ui.UI

@Composable
fun IconTextButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    Button(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(UI.mediumIconSize)
            )
            text?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }
        }
    }
}