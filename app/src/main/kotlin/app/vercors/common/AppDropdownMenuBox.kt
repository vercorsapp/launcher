package app.vercors.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import app.vercors.applyIf
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.ChevronUp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> AppDropdownMenuBox(
    options: Iterable<T>,
    value: T?,
    textConverter: @Composable (T) -> String,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier.width(IntrinsicSize.Max),
    leadingIcon: @Composable (() -> Unit)? = null,
    showScrollbar: Boolean = true,
    optionContent: @Composable RowScope.(T) -> Unit
) {
    val colors = MaterialTheme.colors
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.pointerHoverIcon(
            PointerIcon.Hand,
            overrideDescendants = true
        )
    ) {
        OutlinedTextField(
            value = value?.let { textConverter(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) FeatherIcons.ChevronUp else FeatherIcons.ChevronDown,
                    contentDescription = "Show options"
                )
            }
        )

        ScrollableExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            showScrollbar = showScrollbar,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            options.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onValueChange(it)
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    modifier = Modifier.applyIf<Modifier>(it == value) { background(colors.primary) }
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides if (it == value) colors.onPrimary else colors.onSurface
                    ) {
                        optionContent(it)
                    }
                }
            }
        }
    }
}