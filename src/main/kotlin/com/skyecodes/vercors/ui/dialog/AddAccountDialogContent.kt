package com.skyecodes.vercors.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.skyecodes.vercors.component.dialog.AddAccountDialogComponent
import com.skyecodes.vercors.ui.LocalLocalization
import com.skyecodes.vercors.ui.LocalPalette
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.common.AppTextButton
import com.skyecodes.vercors.ui.common.IconTextButton
import com.skyecodes.vercors.ui.common.appAnimateContentSize
import compose.icons.FeatherIcons
import compose.icons.feathericons.Copy
import compose.icons.feathericons.ExternalLink
import compose.icons.feathericons.X
import compose.icons.feathericons.XCircle

@Composable
fun AddAccountDialogContent(component: AddAccountDialogComponent) {
    val locale = LocalLocalization.current
    val clipboardManager = LocalClipboardManager.current
    val uiState by component.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding).appAnimateContentSize()
    ) {
        Text(locale.addAccount, style = MaterialTheme.typography.h5)
        Text(locale.addAccountInfo, style = MaterialTheme.typography.h6)

        if (uiState.isWaitingLogin) {
            Text(locale.addAccountLogin)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconTextButton(
                    enabled = component.canOpenInBrowser && uiState.hasUrl,
                    onClick = { component.openInBrowser(uiState.url!!) },
                    imageVector = FeatherIcons.ExternalLink,
                    text = locale.openInBrowser
                )
                IconTextButton(
                    enabled = uiState.hasUrl,
                    onClick = { clipboardManager.setText(AnnotatedString(uiState.url!!)) },
                    imageVector = FeatherIcons.Copy,
                    text = locale.copyUrl,
                    colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.error?.let {
                    Icon(FeatherIcons.XCircle, null, Modifier.size(UI.mediumIconSize), MaterialTheme.colors.error)
                }
                Text(
                    text = uiState.phase.localizedText(locale),
                    color = if (uiState.error == null) MaterialTheme.colors.onSurface else MaterialTheme.colors.error
                )
            }
            if (uiState.progress < 0 && uiState.error == null) LinearProgressIndicator()
            else LinearProgressIndicator(uiState.progress)
        }

        uiState.error?.let {
            Text(it.message ?: locale.errorOccurred, color = MaterialTheme.colors.error)
        }

        if (uiState.canClose) {
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
}