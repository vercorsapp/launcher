package com.skyecodes.vercors.ui.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skyecodes.vercors.component.dialog.ErrorDialogComponent
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.AppTextButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.X

@Composable
@Preview
fun ErrorDialogContent(component: ErrorDialogComponent) {
    Column(
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding)
    ) {
        Text(component.title, style = MaterialTheme.typography.h5)

        Column(verticalArrangement = Arrangement.spacedBy(UI.smallPadding)) {
            component.message.forEach { Text(it) }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AppTextButton(component.onClose) {
                Icon(
                    imageVector = FeatherIcons.X,
                    contentDescription = null,
                    modifier = Modifier.size(UI.mediumIconSize)
                )
                Text(
                    text = LocalLocalization.current.close,
                    modifier = Modifier.padding(start = UI.mediumPadding)
                )
            }
        }
    }
}
