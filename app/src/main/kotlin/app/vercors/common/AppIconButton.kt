package app.vercors.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.UI

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    tooltipText: String,
    modifier: Modifier = Modifier.widthIn(min = 16.dp),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    TooltipArea(
        tooltip = { AppTooltip(tooltipText) }
    ) {
        AppButton(
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
            Icon(
                imageVector = imageVector,
                contentDescription = tooltipText,
                modifier = Modifier.size(UI.mediumIconSize)
            )
        }
    }
}