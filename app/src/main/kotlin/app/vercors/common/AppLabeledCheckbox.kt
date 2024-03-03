package app.vercors.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle

@Composable
fun AppLabeledCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.subtitle2,
    textModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    AppCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors
    )
    Text(
        modifier = textModifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                if (onCheckedChange != null) {
                    onCheckedChange(!checked)
                }
            }
        ).pointerHoverIcon(PointerIcon.Hand),
        text = text,
        style = textStyle
    )
}