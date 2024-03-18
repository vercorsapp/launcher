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

package app.vercors.menu

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.account.AccountData
import app.vercors.account.AccountImage
import app.vercors.common.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.User
import org.jetbrains.compose.resources.stringResource
import vercors.ui.generated.resources.Res
import vercors.ui.generated.resources.accounts
import vercors.ui.generated.resources.createInstance

@Composable
fun MenuButtonContent(
    button: MenuButton,
    isSelected: Boolean,
    currentAccount: AccountData?,
    onClick: () -> Unit
) {
    when (button) {
        MenuButton.Accounts -> AccountsButton(currentAccount, stringResource(Res.string.accounts), onClick)
        MenuButton.CreateInstance -> NormalMenuButton(
            false,
            stringResource(Res.string.createInstance),
            FeatherIcons.Plus,
            onClick
        )

        MenuButton.Home -> TabMenuButton(isSelected, AppTab.Home, onClick)
        MenuButton.Instances -> TabMenuButton(isSelected, AppTab.Instances, onClick)
        MenuButton.Search -> TabMenuButton(isSelected, AppTab.Search, onClick)
        MenuButton.Settings -> TabMenuButton(isSelected, AppTab.Settings, onClick)
    }
}

@Composable
private fun AccountsButton(currentAccount: AccountData?, title: String, onClick: () -> Unit) {
    currentAccount?.let {
        MenuButtonFrame(title, false, onClick, 0.dp) {
            AccountImage(it, Modifier.fillMaxSize())
        }
    } ?: NormalMenuButton(false, title, FeatherIcons.User, onClick)
}

@Composable
private fun TabMenuButton(isSelected: Boolean, tab: AppTab, onClick: () -> Unit) {
    NormalMenuButton(isSelected, tab.title, tab.icon, onClick)
}

@Composable
private fun NormalMenuButton(isSelected: Boolean, title: String, icon: ImageVector, onClick: () -> Unit) {
    MenuButtonFrame(title, isSelected, onClick) {
        Icon(icon, title, Modifier.fillMaxSize())
    }
}

@Composable
private fun MenuButtonFrame(
    title: String,
    active: Boolean,
    onClick: () -> Unit,
    contentPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = { AppTooltip(title) },
        tooltipPlacement = TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(30.dp, -(25.dp))
        ),
        delayMillis = 250,
    ) {
        val backgroundColor by appAnimateColorAsState(active, MaterialTheme.colors.primary, Color.Transparent)
        val contentColor by appAnimateColorAsState(
            active,
            MaterialTheme.colors.onPrimary,
            MaterialTheme.colors.onSurface
        )

        AppButton(
            modifier = Modifier.size(50.dp),
            elevation = if (active) ButtonDefaults.elevation() else null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(contentPadding),
            shape = UI.largeRoundedCornerShape,
            onClick = onClick
        ) {
            content()
        }
    }
}