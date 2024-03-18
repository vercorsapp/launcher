/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import app.vercors.LocalConfiguration
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.navigation.title
import app.vercors.notification.NotificationLevel
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.next
import vercors.ui.generated.resources.previous
import vercors.ui.generated.resources.refresh

@Composable
fun ToolbarContent(
    state: ToolbarState,
    onIntent: (ToolbarIntent) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.padding(UI.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
        ) {
            ToolbarButtonContent(
                icon = FeatherIcons.ChevronLeft,
                name = stringResource(Res.string.previous),
                interactionSource = interactionSource,
                enabled = state.hasPreviousScreen,
                onClick = { onIntent(ToolbarIntent.ToolbarClick(ToolbarButton.Previous)) }
            )
            ToolbarButtonContent(
                icon = FeatherIcons.ChevronRight,
                name = stringResource(Res.string.next),
                interactionSource = interactionSource,
                enabled = state.hasNextScreen,
                onClick = { onIntent(ToolbarIntent.ToolbarClick(ToolbarButton.Next)) }
            )
            ToolbarButtonContent(
                icon = FeatherIcons.RefreshCw,
                name = stringResource(Res.string.refresh),
                interactionSource = interactionSource,
                enabled = state.canRefreshScreen,
                onClick = { onIntent(ToolbarIntent.ToolbarClick(ToolbarButton.Refresh)) }
            )
            state.title.forEachIndexed { index, config ->
                if (index != 0) Text(
                    text = " > ",
                    lineHeight = UI.normalLineHeight
                )
                Text(
                    text = config.title,
                    lineHeight = UI.normalLineHeight,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onIntent(ToolbarIntent.TitleClick(config)) }
                    ).pointerHoverIcon(PointerIcon.Hand)
                )
            }
        }
        Spacer(Modifier.weight(1f))

        Row(Modifier.padding(horizontal = UI.largePadding)) {
            if (state.unreadNotifications > 0) {
                BadgedBox(
                    badge = {
                        Badge(
                            backgroundColor = getNotificationBadgeColor(state.maxUnreadNotificationLevel),
                            contentColor = MaterialTheme.colors.onPrimary
                        ) {
                            Text(state.unreadNotifications.toString())
                        }
                    }
                ) {
                    NotificationButtonContent(interactionSource) { onIntent(ToolbarIntent.NotificationButtonClick) }
                }
            } else {
                NotificationButtonContent(interactionSource) { onIntent(ToolbarIntent.NotificationButtonClick) }
            }
        }
        if (!LocalConfiguration.current.systemWindowFrame) {
            Row {
                WindowButtonContent(
                    FeatherIcons.Minus,
                    "Minimize Window"
                ) { onIntent(ToolbarIntent.ToolbarClick(ToolbarButton.Minimize)) }
                WindowButtonContent(
                    FeatherIcons.Square,
                    "Maximize Window"
                ) { onIntent(ToolbarIntent.ToolbarClick(ToolbarButton.Maximize)) }
                WindowButtonContent(
                    FeatherIcons.X,
                    "Close Window",
                    true
                ) { onIntent(ToolbarIntent.ToolbarClick(ToolbarButton.Close)) }
            }
        }

    }
}

@Composable
private fun getNotificationBadgeColor(level: NotificationLevel): Color = when (level) {
    NotificationLevel.INFO -> LocalPalette.current.blue
    NotificationLevel.WARN -> LocalPalette.current.yellow
    NotificationLevel.ERROR -> LocalPalette.current.red
}
